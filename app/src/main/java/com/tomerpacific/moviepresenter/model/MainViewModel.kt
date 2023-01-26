package com.tomerpacific.moviepresenter.model

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tomerpacific.moviepresenter.network.MovieImageCache
import com.tomerpacific.moviepresenter.network.NetworkConnectivityManager
import com.tomerpacific.moviepresenter.repository.MovieRepositoryImpl
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val movieRepository: MovieRepositoryImpl = MovieRepositoryImpl()
    private val networkConnectivityManager: NetworkConnectivityManager = NetworkConnectivityManager()
    private val movieImageCache: MovieImageCache = MovieImageCache()
    val moviesList: MutableState<List<MovieModel>> = mutableStateOf(listOf())
    var inLoadingState: MutableState<Boolean> = mutableStateOf(true)
    var isInternetConnectionAvailable: MutableState<Boolean> = mutableStateOf(true)

    var movieItemPressed: MovieModel? = null

    init {

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

    fun handleNavigationToMovieViewFromMovieCard(movie: MovieModel) {
        movieItemPressed = movie
        inLoadingState.value = true
        fetchMoviePoster()
    }


    private fun fetchMoviePoster() {
        movieItemPressed?.let { movie ->
            movieImageCache.getBitmapFromCache(movie.posterImgPath)?.also { bitmap ->
                movieItemPressed!!.largePosterImgBitmap = bitmap
                inLoadingState.value = false
            } ?: viewModelScope.launch {
                movieItemPressed = movieRepository.fetchMoviePoster(movie)
                inLoadingState.value = false
                movieImageCache.saveBitmapToCache(movie.posterImgPath, movie.largePosterImgBitmap!!)
            }
        }
    }
}