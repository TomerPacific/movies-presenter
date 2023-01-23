package com.tomerpacific.moviepresenter.model

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tomerpacific.moviepresenter.network.NetworkConnectivityManager
import com.tomerpacific.moviepresenter.repository.MovieRepositoryImpl
import com.tomerpacific.moviepresenter.service.SharedPreferencesService
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val movieRepository: MovieRepositoryImpl = MovieRepositoryImpl()
    private val networkConnectivityManager: NetworkConnectivityManager = NetworkConnectivityManager()
    val moviesList: MutableState<List<MovieModel>> = mutableStateOf(listOf())
    var inLoadingState: MutableState<Boolean> = mutableStateOf(true)
    var isInternetConnectionAvailable: MutableState<Boolean> = mutableStateOf(true)

    var movieItemPressed: MovieModel? = null

    init {

        SharedPreferencesService.initializeSharedPrefs(application.applicationContext)

        if (!networkConnectivityManager.isNetworkConnected(application.applicationContext)) {
            inLoadingState.value = false
            isInternetConnectionAvailable.value = false
        } else {
            viewModelScope.launch {
                movieRepository.fetchMovies()?.let { response ->
                    var movies: List<MovieModel> = response.results
                    movies = movieRepository.fetchMoviePosters(movies)
                    moviesList.value = movies
                    inLoadingState.value = false
                }
            }
        }
    }

    fun fetchMoviePoster() {
        movieItemPressed?.let { movie ->
            viewModelScope.launch {
                movieItemPressed = movieRepository.fetchMoviePoster(movie)
                inLoadingState.value = false
            }
        }
    }
}