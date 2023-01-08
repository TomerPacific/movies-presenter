package com.tomerpacific.moviepresenter.model

import android.graphics.Bitmap
import org.json.JSONArray

data class MovieModel(
    val isAdultMovie: Boolean,
    val genreIds: JSONArray,
    val id: Int,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularityScore: Double,
    val posterImagePath: String,
    val releaseData: String,
    var posterImgBitmap: Bitmap?
)