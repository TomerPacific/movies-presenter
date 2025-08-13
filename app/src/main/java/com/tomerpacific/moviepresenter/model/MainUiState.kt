package com.tomerpacific.moviepresenter.model

import com.tomerpacific.moviepresenter.domain.MovieModel

data class MainUiState(
    val isLoading: Boolean = true,
    val moviesList: List<MovieModel>? = null,
    val isInternetConnectionAvailable: Boolean = true,
    val movieItemPressed: MovieModel? = null,
)