package com.tomerpacific.moviepresenter.ui.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomerpacific.moviepresenter.Utils
import com.tomerpacific.moviepresenter.model.MainViewModel

@Composable
fun MovieView(viewModel: MainViewModel) {

    val mainUiState by viewModel.mainUiState.collectAsState()

    mainUiState.movieItemPressed?.let { movie ->
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Scaffold(contentWindowInsets = WindowInsets.safeContent) { innerPadding ->
                Column(modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    when {
                        mainUiState.isLoading -> {
                            CircularProgressBarIndicator()
                        }

                        else -> {
                            Row(horizontalArrangement = Arrangement.Center) {
                                Text(
                                    text = movie.originalTitle,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Row(horizontalArrangement = Arrangement.Center) {
                                movie.largeBackdropImgBitmap?.let {
                                    Image(
                                        bitmap = it.asImageBitmap(),
                                        contentDescription = movie.originalTitle
                                    )
                                }
                            }

                            Row(horizontalArrangement = Arrangement.Center) {
                                Text(
                                    text = "Released : " + Utils.reverseDateFormat(movie.releaseDate),
                                    fontSize = 27.sp
                                )
                            }

                            Row(horizontalArrangement = Arrangement.Center) {
                                Text(
                                    modifier = Modifier.padding(5.dp),
                                    text = movie.movieOverview,
                                    fontSize = 23.sp,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Row(horizontalArrangement = Arrangement.Center) {
                                Icon(
                                    modifier = Modifier.size(35.dp),
                                    imageVector = Icons.Rounded.Star,
                                    contentDescription = "Star Icon",
                                    tint = Color.Yellow
                                )
                                Text(
                                    modifier = Modifier.padding(5.dp),
                                    text = "Rating: " + movie.voteAvg.toString(),
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Center,
                                    color = Utils.getColorRating(movie.voteAvg)
                                )
                                Icon(
                                    modifier = Modifier.size(35.dp),
                                    imageVector = Icons.Rounded.Star,
                                    contentDescription = "Star Icon",
                                    tint = Color.Yellow
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}