package com.tomerpacific.moviepresenter.model

import org.json.JSONArray

data class TMDBResponse(
    val pageNumber: Int,
    val results: JSONArray
)