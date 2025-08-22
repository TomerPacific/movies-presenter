package com.tomerpacific.moviepresenter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tomerpacific.moviepresenter.ui.main.MainViewModel
import com.tomerpacific.moviepresenter.ui.theme.MoviePresenterTheme
import com.tomerpacific.moviepresenter.ui.components.MovieView
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModelProvider
import com.tomerpacific.moviepresenter.data.repository.MovieRepositoryImpl
import com.tomerpacific.moviepresenter.domain.usecase.GetMoviePosterUseCase
import com.tomerpacific.moviepresenter.domain.usecase.GetMoviePostersUseCase
import com.tomerpacific.moviepresenter.domain.usecase.GetMoviesUseCase
import com.tomerpacific.moviepresenter.ui.main.MainViewModelFactory
import com.tomerpacific.moviepresenter.ui.components.MovieList

class MainActivity : ComponentActivity() {

    private lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val moviesRepositoryImpl = MovieRepositoryImpl()
        val viewModelFactory = MainViewModelFactory(
            application = application,
            getMoviesUseCase = GetMoviesUseCase(moviesRepositoryImpl),
            getMoviePostersUseCase = GetMoviePostersUseCase(moviesRepositoryImpl),
            getMoviePosterUseCase = GetMoviePosterUseCase(moviesRepositoryImpl)
        )

        mainViewModel = ViewModelProvider(this, viewModelFactory)
            .get(MainViewModel::class.java)

        setContent {

            MoviePresenterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CreateNavGraph()
                }
            }
        }
    }

    @Composable
    fun CreateNavGraph(
        navController: NavHostController = rememberNavController(),
        startDestination: String = "main"
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination) {
            composable("main") {
                MovieList(
                    mainViewModel,
                    onNavigateToMovieView = {
                    navController.navigate("movie")
                })
            }
            composable("movie") {
                MovieView(viewModel = mainViewModel)
            }
        }
    }
}