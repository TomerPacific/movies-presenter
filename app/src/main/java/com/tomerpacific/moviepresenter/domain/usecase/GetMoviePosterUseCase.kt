package com.tomerpacific.moviepresenter.domain.usecase

import com.tomerpacific.moviepresenter.domain.model.MovieModel
import com.tomerpacific.moviepresenter.domain.repository.MovieRepository

class GetMoviePosterUseCase(private val repository: MovieRepository) {

    suspend operator fun invoke(movie: MovieModel) = repository.fetchMoviePoster(movie)
}