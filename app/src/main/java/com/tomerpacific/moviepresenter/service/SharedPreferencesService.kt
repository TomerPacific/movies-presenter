package com.tomerpacific.moviepresenter.service

import android.content.Context
import android.content.SharedPreferences
import com.tomerpacific.moviepresenter.model.TMDBResponse
import kotlinx.serialization.json.Json

object SharedPreferencesService {

    private lateinit var sharedPreferences: SharedPreferences
    private const val SHARED_PREFS_NAME: String = "MoviePresenterSharedPrefs"

    fun initializeSharedPrefs(applicationContext: Context) {
        sharedPreferences = applicationContext.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setCachedResponseTime(timestamp: Long) {
        sharedPreferences.edit().putLong("cachedResponseTimestamp", timestamp).apply()
    }

    fun getCachedResponseTime(): Long {
        return sharedPreferences.getLong("cachedResponseTimestamp", 0L)
    }

    fun setCachedResponse(response: TMDBResponse) {
        sharedPreferences.edit().putString("cachedResponse", Json.encodeToString(TMDBResponse.serializer(), response)).apply()
    }

    fun getCachedResponse(): TMDBResponse? {
        val cachedResponse: String = sharedPreferences.getString("cachedResponse", "") ?: return null
        return when (cachedResponse.length) {
            0 -> null
            else -> Json.decodeFromString(TMDBResponse.serializer(), cachedResponse)
        }
    }

}