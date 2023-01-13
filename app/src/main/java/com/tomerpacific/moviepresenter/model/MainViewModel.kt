package com.tomerpacific.moviepresenter.model

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tomerpacific.moviepresenter.repository.MovieRepositoryImpl
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val movieRepository: MovieRepositoryImpl = MovieRepositoryImpl()
    val moviesList: MutableState<List<MovieModel>> = mutableStateOf(listOf())
    var inLoadingState: MutableState<Boolean> = mutableStateOf(true)

    var movieItemPressed: MovieModel? = null

    init {
        viewModelScope.launch {
            movieRepository.fetchMovies()?.let { response ->
               var movies: List<MovieModel> = response.results
               movies = movieRepository.fetchMoviePosters(movies)
                moviesList.value = movies
                inLoadingState.value = false
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