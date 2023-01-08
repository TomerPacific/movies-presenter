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
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL

class MainViewModel(application: Application): AndroidViewModel(application) {

    val moviesList: MutableState<List<MovieModel>> = mutableStateOf(listOf())

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
                            val data = JSONObject(line)
                            val response = TMDBResponse(
                                data.getInt("page"),
                                data.getJSONArray("results")
                            )
                            convertResponseToModel(response)
                            fetchMoviePosters()
                        }
                    }
                }
            }
        }
    }

    private fun convertResponseToModel(response: TMDBResponse) {

        val movies: MutableList<MovieModel> = mutableListOf()

        val moviesData: JSONArray = response.results
        for (index in 0 until moviesData.length()) {
            val movie: JSONObject = moviesData.getJSONObject(index)
            val movieModel = MovieModel(
                movie.getBoolean("adult"),
                movie.getJSONArray("genre_ids"),
                movie.getInt("id"),
                movie.getString("original_language"),
                movie.getString("original_title"),
                movie.getString("overview"),
                movie.getDouble("popularity"),
                movie.getString("poster_path"),
                movie.getString("release_date"),
                null
            )
            movies.add(movieModel)
        }

        moviesList.value = movies
    }

    private fun fetchMoviePosters() {
        val movies = moviesList.value
        for (movie in movies) {
            viewModelScope.launch {
                val posterUrl = movie.posterImagePath
                launch(Dispatchers.IO) {
                    val url = URL(Constants.MOVIE_POSTER_ENDPOINT + posterUrl)
                    with(url.openConnection() as HttpURLConnection) {
                        requestMethod = "GET"
                        val bufferedInputStream = BufferedInputStream(inputStream)
                        val img = BitmapFactory.decodeStream(bufferedInputStream)
                        movie.posterImgBitmap = img
                    }
                }
            }
        }
        moviesList.value = movies
    }
}