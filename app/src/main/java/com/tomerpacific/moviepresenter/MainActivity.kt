package com.tomerpacific.moviepresenter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

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
        val movies = viewModel.moviesList.value
        val isLoading = viewModel.inLoadingState.value
        val isInternetConnectionAvailable = viewModel.isInternetConnectionAvailable.value

        Column {
            Row ( Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = Constants.APP_TITLE, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            }
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(movies) { movie ->
                        MovieCard(movie, viewModel, onNavigateToMovieView)
                    }
                }
                NetworkErrorText(isInternetConnectionAvailable)
                CircularProgressBarIndicator(isLoading)
            }
        }
    }

    @Composable
    fun NetworkErrorText(isInternetConnectionAvailable: Boolean) {
        if (!isInternetConnectionAvailable) {
            Text(text = "There is no internet connection. Please check it and try again.",
                fontSize = 25.sp,
                textAlign = TextAlign.Center)
        }
    }
}