package com.example.staysense.database

import android.content.Context
import android.content.SharedPreferences

object UserSession {
    private const val PREF_NAME = "StaySensePrefs"
    private const val KEY_USER_ID = "id"
    private const val KEY_USERNAME = "username"
    private const val KEY_EMAIL = "email"
    private const val KEY_LOGGED_IN = "isLoggedIn"
    private const val KEY_WORDCLOUD_URL = "wordcloudUrl"
    private const val KEY_PASSWORD = "password"

    private const val KEY_FIRST_RUN = "is_first_run"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveUser(context: Context, id: String, username: String, email: String, password: String) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(KEY_USER_ID, id)
            putString(KEY_USERNAME, username)
            putString(KEY_EMAIL, email)
            putString(KEY_PASSWORD, password)
            putBoolean(KEY_LOGGED_IN, true)
            apply()
        }
    }

    fun getUsername(context: Context): String {
        val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(KEY_USERNAME, "") ?: ""
    }

    fun getEmail(context: Context): String {
        val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(KEY_EMAIL, "") ?: ""
    }

    fun getUserId(context: Context): String? {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(KEY_USER_ID, null)
    }

    fun clearUser(context: Context) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(KEY_LOGGED_IN, false)
    }

    fun saveWordCloudUrl(context: Context, url: String) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPref.edit().putString(KEY_WORDCLOUD_URL, url).apply()
    }

    fun getSavedWordCloudUrl(context: Context): String? {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(KEY_WORDCLOUD_URL, null)
    }

    fun isFirstTimeRun(context: Context): Boolean {
        val prefs = getPreferences(context)
        val isFirstRun = prefs.getBoolean(KEY_FIRST_RUN, true)
        if (isFirstRun) {
            prefs.edit().putBoolean(KEY_FIRST_RUN, false).apply()
        }
        return isFirstRun
    }
}