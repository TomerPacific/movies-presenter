package com.tomerpacific.moviepresenter.repository

import android.graphics.BitmapFactory
import com.tomerpacific.moviepresenter.BuildConfig
import com.tomerpacific.moviepresenter.Constants
import com.tomerpacific.moviepresenter.model.MovieModel
import com.tomerpacific.moviepresenter.model.TMDBResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL

class MovieRepositoryImpl: MovieRepository {
    override suspend fun fetchMovies(): TMDBResponse? {
        var response: TMDBResponse? = null
        coroutineScope {
            val endpoint: String = Constants.POPULAR_MOVIES_ENDPOINT + BuildConfig.TMDB_API_KEY
            launch(Dispatchers.IO) {
                val url = URL(endpoint)
                with(url.openConnection() as HttpURLConnection) {
                    requestMethod = "GET"

                    inputStream.bufferedReader().use {
                        it.lines().forEach { line ->
                            response = Json.decodeFromString<TMDBResponse>(line)
                        }
                    }
                }
            }
        }
        return response
    }

    override suspend fun fetchMoviePosters(movies: List<MovieModel>): List<MovieModel> {
        for (movie in movies) {
            coroutineScope {
                launch {
                    val posterUrl = movie.posterImgPath
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
                }
            }
        }

        return movies
    }

    override suspend fun fetchMoviePoster(movie: MovieModel): MovieModel {
        val posterUrl = movie.backdropImgPath
        val endpoint: String = Constants.MOVIE_POSTER_ENDPOINT +
                Constants.MOVIE_POSTER_LARGE_SIZE +
                posterUrl
        coroutineScope {
            launch(Dispatchers.IO) {
                val url = URL(endpoint)
                with(url.openConnection() as HttpURLConnection) {
                    requestMethod = "GET"
                    val bufferedInputStream = BufferedInputStream(inputStream)
                    val img = BitmapFactory.decodeStream(bufferedInputStream)
                    movie.largeBackdropImgBitmap = img
                }
            }
        }

        return movie
    }
}