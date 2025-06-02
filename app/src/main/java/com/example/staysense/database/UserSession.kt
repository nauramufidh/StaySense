package com.example.staysense.database

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

object UserSession {
    private const val PREF_NAME = "StaySensePrefs"
    private const val KEY_USER_ID = "id"
    private const val KEY_USERNAME = "username"
    private const val KEY_EMAIL = "email"
    private const val KEY_PASSWORD = "password"
    private const val KEY_CONFIRM_PASSWORD = "confirmPassword"
    private const val KEY_LOGGED_IN = "isLoggedIn"

    fun saveUser(context: Context, id: String) {
        val sharedPref = context.getSharedPreferences("StaySensePrefs", AppCompatActivity.MODE_PRIVATE)
        sharedPref.edit().putString(KEY_USER_ID, id).putBoolean(KEY_LOGGED_IN, true).apply()

    }

//    fun saveUser(context: Context, id: String, username: String, email: String?, password: String, confirmPassword: String?) {
//
//        val sharedPref = context.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE)
//        with(sharedPref.edit()) {
//            putString(KEY_USER_ID, id)
//            putString(KEY_USERNAME, username)
//            putString(KEY_EMAIL, email)
//            putString(KEY_PASSWORD, password)
//            putString(KEY_CONFIRM_PASSWORD, confirmPassword)
//        }
//    }



    fun getUsername(context: Context): String {
        val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(KEY_USERNAME, "") ?: ""
    }

    fun getEmail(context: Context): String {
        val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(KEY_EMAIL, "") ?: ""
    }

    fun getUserId(context: Context): String? {
        val sharedPref = context.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        return sharedPref.getString(KEY_USER_ID, null)
    }

    fun clearUser(context: Context) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        sharedPref.edit().clear().apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        return sharedPref.getBoolean(KEY_LOGGED_IN, false)
    }
}