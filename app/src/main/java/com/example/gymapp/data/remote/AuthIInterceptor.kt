package com.example.gymapp.data.remote


import com.example.gymapp.domain.repository.SessionStorage
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val session: SessionStorage) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = session.getToken()
        val request = chain.request().newBuilder().apply {
            if (token != null) addHeader("Authorization", "Bearer $token")
        }.build()
        return chain.proceed(request)
    }
}
