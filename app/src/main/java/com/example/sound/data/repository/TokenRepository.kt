package com.example.sound.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class TokenRepository(context: Context) {
    private val pref = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    fun putToken(token: String) {
        pref.edit() { putString("token", token) }
    }

    fun getToken(): String? {
        return pref.getString("token", null)
    }

    fun clearToken() {
        pref.edit() { remove("token") }
    }
}