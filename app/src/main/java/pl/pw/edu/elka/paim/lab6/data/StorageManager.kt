package pl.pw.edu.elka.paim.lab6.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.serialization.json.Json

object StorageManager {
    private const val SHARED_PREFERENCES_NAME = "kittyCode"

    private const val SEARCH_HISTORY_KEY = "search_history"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            SHARED_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
    }

    fun getSearchHistory(context: Context): List<String> {
        val sharedPrefs = getSharedPreferences(context)
        val savedHistory = sharedPrefs.getString(SEARCH_HISTORY_KEY, null) ?: "{[]}"    // default to empty list

        return  Json.decodeFromString<List<String>>(savedHistory)
    }

    fun saveSearchHistory(context: Context, newHistory: List<String>) {
        val sharedPrefs = getSharedPreferences(context)

        sharedPrefs.edit { putString(SEARCH_HISTORY_KEY, Json.encodeToString(newHistory)) }
    }
}