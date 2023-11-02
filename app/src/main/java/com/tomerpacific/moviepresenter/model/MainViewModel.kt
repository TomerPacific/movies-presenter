package com.tomerpacific.moviepresenter.model

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tomerpacific.moviepresenter.cache.MovieImageCache
import com.tomerpacific.moviepresenter.network.NetworkConnectivityManager
import com.tomerpacific.moviepresenter.repository.MovieRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val movieRepository: MovieRepositoryImpl = MovieRepositoryImpl()
    private val networkConnectivityManager: NetworkConnectivityManager = NetworkConnectivityManager()
    private val movieImageCache: MovieImageCache = MovieImageCache()

    private val _moviesList: MutableStateFlow<List<MovieModel>> = MutableStateFlow(listOf())
    val moviesList: StateFlow<List<MovieModel>> = _moviesList

    private val _inLoadingState: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val inLoadingState: StateFlow<Boolean> = _inLoadingState

    private val _isInternetConnectionAvailable: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isInternetConnectionAvailable: StateFlow<Boolean> = _isInternetConnectionAvailable

    var movieItemPressed: MovieModel? = null

    init {

        if (!networkConnectivityManager.isNetworkConnected(application.applicationContext)) {
            _inLoadingState.value = false
            _isInternetConnectionAvailable.value = false
        } else {
            viewModelScope.launch {
                movieRepository.fetchMovies()?.let { response ->
                    var movies: List<MovieModel> = response.results
                    movies = movieRepository.fetchMoviePosters(movies)
                    _moviesList.value = movies
                    _inLoadingState.value = false
                }
            }
        }
    }

    fun handleNavigationToMovieViewFromMovieCard(movie: MovieModel) {
        movieItemPressed = movie
        _inLoadingState.value = true
        fetchMoviePoster(movie)
    }


    private fun fetchMoviePoster(movie: MovieModel) {
            val imagePath: String = when (movie.backdropImgPath) {
                null -> movie.posterImgPath
                else -> movie.backdropImgPath
            }

            movieImageCache.getBitmapFromCache(imagePath)?.also { bitmap ->
                when (imagePath) {
                    movie.posterImgPath -> movieItemPressed!!.smallPosterImgBitmap = bitmap
                    else -> movieItemPressed!!.largeBackdropImgBitmap = bitmap
                }
                _inLoadingState.value = false
            } ?: viewModelScope.launch {
                movieItemPressed = movieRepository.fetchMoviePoster(movie)
                _inLoadingState.value = false
                val bitmap: Bitmap? = when (imagePath) {
                    movie.posterImgPath -> movie.smallPosterImgBitmap
                    else -> movie.largeBackdropImgBitmap
                }

                bitmap?.let {
                    movieImageCache.saveBitmapToCache(imagePath, it)
                }

            }
        }
}