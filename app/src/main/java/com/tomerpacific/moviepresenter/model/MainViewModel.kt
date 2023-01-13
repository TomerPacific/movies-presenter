package com.tomerpacific.moviepresenter.model

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tomerpacific.moviepresenter.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json.Default.decodeFromString
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL

class MainViewModel(application: Application): AndroidViewModel(application) {

    val moviesList: MutableState<List<MovieModel>> = mutableStateOf(listOf())
    var inLoadingState: MutableState<Boolean> = mutableStateOf(true)

    var movieItemPressed: MovieModel? = null

    init {
        viewModelScope.launch {
            val ai: ApplicationInfo = application.packageManager
                .getApplicationInfo(application.packageName, PackageManager.GET_META_DATA)
            val apiKey = ai.metaData[Constants.TMDB_META_DATA_KEY]

            launch(Dispatchers.IO) {
                val url = URL(Constants.POPULAR_MOVIES_ENDPOINT + apiKey)
                with(url.openConnection() as HttpURLConnection) {
                    requestMethod = "GET"

                    inputStream.bufferedReader().use {
                        it.lines().forEach { line ->
                            val response: TMDBResponse = decodeFromString(TMDBResponse.serializer(), line)
                            fetchMoviePosters(response.results)
                        }
                    }
                }
            }
        }
    }


    private fun fetchMoviePosters(movies: List<MovieModel>) {

        for (movie in movies) {
            viewModelScope.launch {
                val posterUrl = movie.poster_path
                val endpoint: String = Constants.MOVIE_POSTER_ENDPOINT +
                        Constants.MOVIE_POSTER_SMALL_SIZE +
                        posterUrl
                val job = launch(Dispatchers.IO) {
                        val url = URL(endpoint)
                        with(url.openConnection() as HttpURLConnection) {
                            requestMethod = "GET"
                            val bufferedInputStream = BufferedInputStream(inputStream)
                            val img = BitmapFactory.decodeStream(bufferedInputStream)
                            movie.smallPosterImgBitmap = img
                        }
                    }
                job.join()
                moviesList.value = movies
                inLoadingState.value = false
            }
        }
    }

    fun fetchMoviePoster() {
        viewModelScope.launch {
            val posterUrl = movieItemPressed?.poster_path
            val endpoint: String = Constants.MOVIE_POSTER_ENDPOINT +
                    Constants.MOVIE_POSTER_LARGE_SIZE +
                    posterUrl
            launch(Dispatchers.IO) {
                val url = URL(endpoint)
                with(url.openConnection() as HttpURLConnection) {
                    requestMethod = "GET"
                    val bufferedInputStream = BufferedInputStream(inputStream)
                    val img = BitmapFactory.decodeStream(bufferedInputStream)
                    movieItemPressed?.largePosterImgBitmap = img
                    inLoadingState.value = false
                }
            }
        }
    }
}