package com.tomerpacific.moviepresenter.domain.usecase

import com.tomerpacific.moviepresenter.domain.repository.MovieRepository

class GetMoviesUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke() = repository.fetchMovies()
}