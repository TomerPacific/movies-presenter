package com.tomerpacific.moviepresenter.ui.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tomerpacific.moviepresenter.domain.usecase.GetMoviePosterUseCase
import com.tomerpacific.moviepresenter.domain.usecase.GetMoviePostersUseCase
import com.tomerpacific.moviepresenter.domain.usecase.GetMoviesUseCase

class MainViewModelFactory(private val application: Application,
                           private val getMoviesUseCase: GetMoviesUseCase,
                           private val getMoviePostersUseCase: GetMoviePostersUseCase,
                           private val getMoviePosterUseCase: GetMoviePosterUseCase): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(application, getMoviesUseCase, getMoviePostersUseCase, getMoviePosterUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}