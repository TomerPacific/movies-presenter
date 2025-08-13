package com.tomerpacific.moviepresenter.model

import com.tomerpacific.moviepresenter.domain.model.MovieModel
import kotlinx.serialization.Serializable

@Serializable
data class TMDBResponse(
    val page: Int,
    val results: List<MovieModel>,
    val total_pages: Int,
    val total_results: Int
)