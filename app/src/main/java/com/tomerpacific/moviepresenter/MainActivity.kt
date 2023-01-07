package com.tomerpacific.moviepresenter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tomerpacific.moviepresenter.model.MainViewModel
import com.tomerpacific.moviepresenter.ui.theme.MoviePresenterTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            MoviePresenterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        Row ( Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = "Movie Presenter", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                        }
                        MoviesList()
                    }
                }
            }
        }
    }

    @Composable
    fun MoviesList() {
        val movies = viewModel.moviesList.value

        LazyColumn {
            items(movies) { movie ->
                Row {
                   Text(text = movie.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}