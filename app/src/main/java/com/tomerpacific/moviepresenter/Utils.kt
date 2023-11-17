package com.tomerpacific.moviepresenter

import androidx.compose.ui.graphics.Color

class Utils {

    companion object {

        val GREAT_RATING_COLOR = Color(40, 180, 99)
        val AVERAGE_RATING_COLOR = Color(212, 172, 13)
        val POOR_RATING_COLOR = Color(203, 67, 53)


        fun reverseDateFormat(date: String):String {
            val splitDate: List<String> = date.split("-")
            return splitDate[2] + "-" + splitDate[1] + "-" + splitDate[0]
        }

        fun getColorRating(movieRating: Double): Color {
            return when(movieRating) {
                 in 8.0..10.0 -> GREAT_RATING_COLOR
                 in 6.5..7.9 -> AVERAGE_RATING_COLOR
                 else -> POOR_RATING_COLOR
            }
        }
    }


}