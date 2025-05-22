package com.example.sound.data.repository

import android.content.Context
import androidx.core.content.edit

class TokenRepository(context: Context) {
    private val pref = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    fun putAuthInfo(token: String, username: String) {
        pref.edit() {
            putString("token", token)
            putString("username", username)
        }
    }

    fun isLoggedIn(): Boolean {
        return getToken() != null
    }

    fun getUsername(): String? {
        return pref.getString("username", null)
    }

    fun getToken(): String? {
        return pref.getString("token", null)
    }

    fun clear() {
        pref.edit() {
            remove("token")
            remove("username")
        }
    }
}