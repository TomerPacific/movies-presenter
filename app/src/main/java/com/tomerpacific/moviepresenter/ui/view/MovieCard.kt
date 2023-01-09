package com.tomerpacific.moviepresenter.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomerpacific.moviepresenter.model.MovieModel

@Composable
fun MovieCard(movie: MovieModel) {
    Card(
        border = BorderStroke(2.dp, Color.Cyan),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Text(
                modifier = Modifier.padding(5.dp),
                text = movie.originalTitle,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            movie.posterImgBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = movie.originalTitle
                )
            }

        }
    }
}