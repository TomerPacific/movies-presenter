package com.tomerpacific.moviepresenter.model

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tomerpacific.moviepresenter.cache.MovieImageCache
import com.tomerpacific.moviepresenter.domain.model.MovieModel
import com.tomerpacific.moviepresenter.domain.usecase.GetMoviePosterUseCase
import com.tomerpacific.moviepresenter.domain.usecase.GetMoviePostersUseCase
import com.tomerpacific.moviepresenter.network.NetworkConnectivityManager
import com.tomerpacific.moviepresenter.domain.usecase.GetMoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    application: Application,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getMoviePostersUseCase: GetMoviePostersUseCase,
    private val getMoviePosterUseCase: GetMoviePosterUseCase): AndroidViewModel(application) {

    private val networkConnectivityManager: NetworkConnectivityManager = NetworkConnectivityManager()
    private val movieImageCache: MovieImageCache = MovieImageCache()

    private val _mainUiState = MutableStateFlow(MainUiState())
    val mainUiState: StateFlow<MainUiState> = _mainUiState.asStateFlow()

    init {

        if (!networkConnectivityManager.isNetworkConnected(application.applicationContext)) {
            _mainUiState.update {
                it.copy(
                    isLoading = false,
                    isInternetConnectionAvailable = false,
                    moviesList = emptyList()
                )
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                getMoviesUseCase().let { response ->
                    response?.let {
                        var movies: List<MovieModel> = it.results
                        movies = getMoviePostersUseCase(movies)
                        withContext(Dispatchers.Main) {
                            _mainUiState.update { state ->
                                state.copy(
                                    isLoading = false,
                                    isInternetConnectionAvailable = true,
                                    moviesList = movies
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun handleNavigationToMovieViewFromMovieCard(movie: MovieModel) {

        _mainUiState.update {
            it.copy(
                isLoading = true,
                isInternetConnectionAvailable = true,
                movieItemPressed = movie
            )
        }

        fetchMoviePoster(movie)
    }


    private fun fetchMoviePoster(movie: MovieModel) {
            val imagePath: String = when (movie.backdropImgPath) {
                null -> movie.posterImgPath
                else -> movie.backdropImgPath
            }

            movieImageCache.getBitmapFromCache(imagePath)?.also { bitmap ->
                when (imagePath) {
                    movie.posterImgPath -> movie.smallPosterImgBitmap = bitmap
                    else -> movie.largeBackdropImgBitmap = bitmap
                }
            } ?:
                viewModelScope.launch(Dispatchers.IO) {
                    val updatedMovieWithPoster = getMoviePosterUseCase(movie)

                    withContext(Dispatchers.Main) {
                        _mainUiState.update {
                            it.copy(
                                isLoading = false,
                                isInternetConnectionAvailable = true,
                                movieItemPressed = updatedMovieWithPoster
                            )
                        }
                    }

                    val bitmap: Bitmap? = when (imagePath) {
                        movie.posterImgPath -> movie.smallPosterImgBitmap
                        else -> movie.largeBackdropImgBitmap
                    }

                    bitmap?.let {
                        movieImageCache.saveBitmapToCache(imagePath, it)
                    }
                }
        }

    fun fetchMoreMovies() {

        _mainUiState.update {
            it.copy(
                isLoading = true,
                isInternetConnectionAvailable = true,
                moviesList = it.moviesList
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            getMoviesUseCase()?.let { response ->
                var movies: List<MovieModel> = response.results
                movies = getMoviePostersUseCase(movies)
                withContext(Dispatchers.Main) {
                    _mainUiState.update {
                        it.copy(
                            isLoading = false,
                            isInternetConnectionAvailable = true,
                            moviesList = (it.moviesList ?: emptyList()) + movies
                        )
                    }
                }
            }
        }
    }
}