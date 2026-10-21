package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.AppLanguage

class AppPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("cropx_settings", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LANGUAGE = "app_language"
        private const val KEY_DARK_MODE = "app_dark_mode"
        private const val KEY_DARK_MODE_SET = "app_dark_mode_explicitly_set"
    }

    var language: AppLanguage
        get() {
            val code = prefs.getString(KEY_LANGUAGE, "bn") ?: "bn"
            return if (code == "en") AppLanguage.ENGLISH else AppLanguage.BANGLA
        }
        set(value) {
            prefs.edit().putString(KEY_LANGUAGE, value.code).apply()
        }

    var isDarkMode: Boolean
        get() = prefs.getBoolean(KEY_DARK_MODE, false)
        set(value) {
            prefs.edit()
                .putBoolean(KEY_DARK_MODE, value)
                .putBoolean(KEY_DARK_MODE_SET, true)
                .apply()
        }

    val isDarkModeExplicitlySet: Boolean
        get() = prefs.getBoolean(KEY_DARK_MODE_SET, false)
}
