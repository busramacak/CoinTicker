package com.bmprj.cointicker.utils

import android.content.Context
import android.preference.PreferenceManager
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomSharedPreference @Inject constructor(
    @ApplicationContext context: Context,
) {

    private val customSharedPreference = PreferenceManager.getDefaultSharedPreferences(context)

    fun getTime() = customSharedPreference.getLong(TIME, 0)

    fun saveTime(time: Long) {
        customSharedPreference.edit() {
            putLong(TIME, time)
        }
    }

    companion object {
        const val TIME = "time"
    }

}

