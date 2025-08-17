package com.tomerpacific.moviepresenter.ui.main

import com.tomerpacific.moviepresenter.domain.model.MovieModel

data class MainUiState(
    val isLoading: Boolean = true,
    val moviesList: List<MovieModel>? = null,
    val isInternetConnectionAvailable: Boolean = true,
    val movieItemPressed: MovieModel? = null,
)