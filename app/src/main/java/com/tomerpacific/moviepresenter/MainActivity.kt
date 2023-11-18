package com.tomerpacific.moviepresenter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tomerpacific.moviepresenter.model.MainViewModel
import com.tomerpacific.moviepresenter.ui.theme.MoviePresenterTheme
import com.tomerpacific.moviepresenter.ui.view.CircularProgressBarIndicator
import com.tomerpacific.moviepresenter.ui.view.MovieCard
import com.tomerpacific.moviepresenter.ui.view.MovieView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val APP_TITLE = "Movie Presenter"
    private val itemIndexToShowScrollTopTopButton: Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            MoviePresenterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavGraph()
                }
            }
        }
    }

    @Composable
    fun NavGraph(
        navController: NavHostController = rememberNavController(),
        startDestination: String = "main"
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination) {
            composable("main") {
                MoviesList(onNavigateToMovieView = {
                    navController.navigate("movie")
                })
            }
            composable("movie") {
                MovieView(viewModel = viewModel)
            }
        }
    }


    @Composable
    fun MoviesList(onNavigateToMovieView: () -> Unit) {

        val movies by  viewModel.moviesList.collectAsState()
        val isLoading by viewModel.inLoadingState.collectAsState()
        val isInternetConnectionAvailable by viewModel.isInternetConnectionAvailable.collectAsState()
        val lazyListState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        val userReachedBottomOfColumn = didUserReachBottomOfColumn(lazyListState = lazyListState, bufferFromBottom = 3)

        LaunchedEffect(userReachedBottomOfColumn){
            if (userReachedBottomOfColumn) {
                viewModel.fetchMoreMovies()
            }
        }

        val shouldShowCircularProgressBar: Boolean = isLoading || userReachedBottomOfColumn

        Column {
            Row ( Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = APP_TITLE, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            }
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    state = lazyListState
                ) {
                    items(movies) { movie ->
                        MovieCard(movie, viewModel, onNavigateToMovieView)
                    }
                }
                NetworkErrorText(isInternetConnectionAvailable)
                CircularProgressBarIndicator(shouldShowCircularProgressBar)
            }
        }
        ScrollToTopButton(coroutineScope, listState = lazyListState)
    }

    @Composable
    fun NetworkErrorText(isInternetConnectionAvailable: Boolean) {
        if (!isInternetConnectionAvailable) {
            Text(text = "There is no internet connection. Please check it and try again.",
                fontSize = 25.sp,
                textAlign = TextAlign.Center)
        }
    }

    @Composable
    fun didUserReachBottomOfColumn(lazyListState: LazyListState, bufferFromBottom: Int): Boolean {
        val isUserAtBottomOfColumn by remember {
            derivedStateOf {
                val layoutInfo = lazyListState.layoutInfo
                val visibleItemsInfo = layoutInfo.visibleItemsInfo
                if (layoutInfo.totalItemsCount == 0) {
                    false
                } else {
                    val lastVisibleItem = visibleItemsInfo.last()

                    lastVisibleItem.index >= layoutInfo.totalItemsCount - 1 - bufferFromBottom
                }
            }
        }

        return isUserAtBottomOfColumn
    }

    @Composable
    fun ScrollToTopButton(coroutineScope: CoroutineScope, listState: LazyListState) {
        var scrollToTopButtonVisibility by remember {
            mutableStateOf(false)
        }

        scrollToTopButtonVisibility = shouldShowScrollTopTopButton(listState)

        AnimatedVisibility(visible = scrollToTopButtonVisibility,
            enter = fadeIn(),
            exit = fadeOut()) {
            Box(modifier = Modifier.fillMaxSize()) {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .background(Color.White, CircleShape)
                        .then(Modifier.size(50.dp))
                        .border(3.dp, Color.Black, shape = CircleShape),
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    }) {
                    Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Up arrow")
                }
            }
        }
    }

    private fun shouldShowScrollTopTopButton(listState: LazyListState): Boolean {
        return listState.firstVisibleItemIndex >= itemIndexToShowScrollTopTopButton
    }
}