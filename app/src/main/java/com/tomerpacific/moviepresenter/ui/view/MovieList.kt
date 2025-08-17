package com.tomerpacific.moviepresenter.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomerpacific.moviepresenter.model.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

private const val APP_TITLE = "Movies Presenter"
private const val itemIndexToShowScrollToTopButton: Int = 10

@Composable
fun MovieList(
    mainViewModel: MainViewModel,
    onNavigateToMovieView: () -> Unit
) {
    val mainUiState by mainViewModel.mainUiState.collectAsState()
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val shouldShowScrollToTopButton by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex > itemIndexToShowScrollToTopButton }
    }

    var uniqueMovieIndex = 0

    Scaffold(
        contentWindowInsets = WindowInsets.safeContent
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = APP_TITLE,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val movies = mainUiState.moviesList ?: emptyList()

                    items(
                        items = movies,
                        key = { movie -> "${movie.movieId}_${uniqueMovieIndex++}" }
                    ) { movie ->
                        MovieCard(
                            movie = movie,
                            viewModel = mainViewModel,
                            onNavigateToMovieView = onNavigateToMovieView
                        )
                    }

                    if (movies.isEmpty() && !mainUiState.isInternetConnectionAvailable) {
                        item {
                            NetworkErrorText()
                        }
                    }

                    if (mainUiState.isLoading) {
                        item {
                            CircularProgressBarIndicator()
                        }
                    }
                }

                LaunchedEffect(lazyListState, mainUiState.moviesList) {
                    snapshotFlow {
                        lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                    }
                        .distinctUntilChanged()
                        .collect { lastVisibleIndex ->
                            val movies = mainUiState.moviesList
                            if (!mainUiState.isLoading &&
                                movies != null &&
                                lastVisibleIndex != null &&
                                lastVisibleIndex >= movies.lastIndex - 3
                            ) {
                                mainViewModel.fetchMoreMovies()
                            }
                        }
                }

                if (shouldShowScrollToTopButton) {
                    ScrollToTopButton(coroutineScope, listState = lazyListState)
                }
            }
        }
    }
}

@Composable
fun NetworkErrorText() {
    Text(text = "There is no internet connection. Please check it and try again.",
        fontSize = 25.sp,
        textAlign = TextAlign.Center)
}

@Composable
fun ScrollToTopButton(coroutineScope: CoroutineScope, listState: LazyListState) {

    AnimatedVisibility(
        visible = true,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
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
