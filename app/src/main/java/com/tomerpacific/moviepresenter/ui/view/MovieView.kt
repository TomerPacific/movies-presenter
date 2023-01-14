package com.tomerpacific.moviepresenter.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomerpacific.moviepresenter.model.MainViewModel

@Composable
fun MovieView(viewModel: MainViewModel) {

    val isLoading: Boolean = viewModel.inLoadingState.value

    viewModel.fetchMoviePoster()

    viewModel.movieItemPressed?.let { movie ->
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            CircularProgressBarIndicator(shouldBeDisplayed = isLoading)
            Column(modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Row(horizontalArrangement = Arrangement.Center) {
                    Text(text = movie.original_title,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                Row(horizontalArrangement = Arrangement.Center) {
                    movie.largePosterImgBitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = movie.original_title
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.Center) {
                    Text(text = "Released : " + movie.release_date, fontSize = 27.sp)
                }

                Row(horizontalArrangement = Arrangement.Center) {
                    Text(modifier = Modifier.padding(5.dp),
                        text = movie.overview,
                        fontSize = 23.sp,
                        textAlign = TextAlign.Center)
                }
            }
        }
    }


}