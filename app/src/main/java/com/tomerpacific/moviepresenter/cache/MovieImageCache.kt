package com.tomerpacific.moviepresenter.cache

import android.graphics.Bitmap
import android.util.LruCache


class MovieImageCache {

    private var memoryCache: LruCache<String, Bitmap>
    private val MEGA_BYTE = 1024
    private val AMOUNT_OF_RAM = 8

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / MEGA_BYTE).toInt()

        val cacheSize = maxMemory / AMOUNT_OF_RAM

        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {

            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount / MEGA_BYTE
            }
        }
    }

    fun saveBitmapToCache(key: String, bitmap: Bitmap) {
        memoryCache.put(key, bitmap)
    }

    fun getBitmapFromCache(key: String): Bitmap? {
        return memoryCache.get(key)
    }

}