package com.tomerpacific.moviepresenter.network

import com.tomerpacific.moviepresenter.model.TMDBResponse
import kotlin.math.abs

class NetworkResponseCache {

    private var movieResponse: TMDBResponse? = null
    private var responseSetTimestamp: Long? = null
    private val CACHE_OUTDATED_LIMIT: Long = 86400000

    fun setResponse(response: TMDBResponse) {
        movieResponse = response
        responseSetTimestamp = System.currentTimeMillis()
    }

    fun getCachedResponse(): TMDBResponse? {
        return movieResponse
    }

    fun isCacheResponseOutdated(): Boolean {
        if (responseSetTimestamp == null) {
            return true
        }

        val difference = abs(responseSetTimestamp!! - System.currentTimeMillis())

        return difference >= CACHE_OUTDATED_LIMIT

    }

    fun getCachedResponseTimestamp(): Long {
        return responseSetTimestamp ?: 0
    }
}