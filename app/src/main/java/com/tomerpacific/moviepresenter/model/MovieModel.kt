package com.tomerpacific.moviepresenter.model

data class MovieModel(
    val title: String,
    val yearOfRelease: String,
    val rating: Double,
    val duration: String,
    val certification: Char,
    val description: String
)