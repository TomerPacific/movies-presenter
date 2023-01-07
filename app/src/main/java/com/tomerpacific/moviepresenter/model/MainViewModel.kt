package com.tomerpacific.moviepresenter.model

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tomerpacific.moviepresenter.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

@RequiresApi(Build.VERSION_CODES.N)
class MainViewModel(application: Application): AndroidViewModel(application) {

    val moviesList: MutableState<List<MovieModel>> = mutableStateOf(listOf())

    init {
        viewModelScope.launch {
            val ai: ApplicationInfo = application.packageManager
                .getApplicationInfo(application.packageName, PackageManager.GET_META_DATA)
            val apiKey = ai.metaData[Constants.TMDB_META_DATA_KEY]

            launch(Dispatchers.IO) {
                val url = URL(Constants.ENDPOINT + apiKey)
                with(url.openConnection() as HttpURLConnection) {
                    requestMethod = "GET"

                    inputStream.bufferedReader().use {
                        it.lines().forEach { line ->

                        }
                    }
                }
            }

        }
    }


}