package com.tomerpacific.moviepresenter.model

data class MainUiState(
    val isLoading: Boolean = false,
    val moviesList: List<MovieModel>? = null,
    val isInternetConnectionAvailable: Boolean = true,
    val movieItemPressed: MovieModel? = null,
)