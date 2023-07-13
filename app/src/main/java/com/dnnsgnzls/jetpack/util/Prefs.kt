package com.dnnsgnzls.jetpack.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class Prefs {
    companion object {

        private const val PREF_SAVE_TIME = "pref_save_time"
        private var prefs: SharedPreferences? = null

        @Volatile
        private var instance: Prefs? = null

        operator fun invoke(context: Context): Prefs = instance ?: synchronized(this) {
            instance ?: buildHelper(context).also {
                instance = it
            }
        }

        private fun buildHelper(context: Context): Prefs {
            val sharedPreferencesName = "${context.packageName}_preferences"
            prefs = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
            return Prefs()
        }
    }

    fun saveUpdateTime(time: Long) {
        prefs?.edit(commit = true) { putLong(PREF_SAVE_TIME, time) }
    }

    fun getUpdateTime() = prefs?.getLong(PREF_SAVE_TIME, 0) ?: 0

    fun getCacheDuration() = prefs?.getString("pref_cache_duration", "")
}