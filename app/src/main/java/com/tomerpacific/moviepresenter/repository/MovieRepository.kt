package com.tomerpacific.moviepresenter.repository

import com.tomerpacific.moviepresenter.model.MovieModel
import com.tomerpacific.moviepresenter.model.TMDBResponse

interface MovieRepository {

    suspend fun fetchMovies(): TMDBResponse?

    suspend fun fetchMoviePosters(movies: List<MovieModel>): List<MovieModel>

    suspend fun fetchMoviePoster(movie: MovieModel): MovieModel

}