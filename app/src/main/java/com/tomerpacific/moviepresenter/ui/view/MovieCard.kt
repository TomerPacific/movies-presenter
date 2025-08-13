package com.tomerpacific.moviepresenter.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomerpacific.moviepresenter.model.MainViewModel
import com.tomerpacific.moviepresenter.domain.model.MovieModel

@Composable
fun MovieCard(movie: MovieModel,
              viewModel: MainViewModel,
              onNavigateToMovieView: () -> Unit) {
    Card(
        border = BorderStroke(2.dp, Color.Cyan),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clickable {
                viewModel.handleNavigationToMovieViewFromMovieCard(movie)
                onNavigateToMovieView()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = movie.originalTitle,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            movie.smallPosterImgBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = movie.originalTitle
                )
            }
        }
    }
}