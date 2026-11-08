package com.example.gymapp.domain.repository



interface SessionStorage {
    fun saveToken(token: String)
    fun getToken(): String?
    fun clear()
}