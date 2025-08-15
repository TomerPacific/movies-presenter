package com.tomerpacific.moviepresenter.domain.usecase

import com.tomerpacific.moviepresenter.domain.model.MovieModel
import com.tomerpacific.moviepresenter.domain.repository.MovieRepository

class GetMoviePostersUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(movies: List<MovieModel>) = repository.fetchMoviePosters(movies)
}
