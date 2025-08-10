package com.tomerpacific.moviepresenter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tomerpacific.moviepresenter.model.MainViewModel
import com.tomerpacific.moviepresenter.ui.theme.MoviePresenterTheme
import com.tomerpacific.moviepresenter.ui.view.MovieView
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.tomerpacific.moviepresenter.ui.view.MovieList

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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