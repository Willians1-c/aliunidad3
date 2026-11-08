package com.example.gymapp.data.local

import android.content.Context
import com.example.gymapp.domain.repository.SessionStorage

class SharedPrefsSessionStorage(context: Context) : SessionStorage {

    private val prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)

    override fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    override fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    override fun clear() {
        prefs.edit().remove(KEY_TOKEN).apply()
    }

    private companion object {
        const val KEY_TOKEN = "token"
    }
}