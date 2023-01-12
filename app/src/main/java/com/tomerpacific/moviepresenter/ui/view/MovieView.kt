package com.tomerpacific.moviepresenter.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tomerpacific.moviepresenter.model.MovieModel

@Composable
fun MovieView(movie: MovieModel) {
    Column(verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.Center) {
            Text(text = movie.original_title,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row(horizontalArrangement = Arrangement.Center) {
            Text(text = movie.release_date, fontSize = 20.sp)
        }

        Row(horizontalArrangement = Arrangement.Center) {
            Text(text = movie.overview, fontSize = 15.sp)
        }
    }
}