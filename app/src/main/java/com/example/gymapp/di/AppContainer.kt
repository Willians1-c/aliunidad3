package com.example.gymapp.di



import android.content.Context
import com.example.gymapp.data.local.SharedPrefsSessionStorage
import com.example.gymapp.data.remote.AuthInterceptor
import com.example.gymapp.data.remote.api.AuthApi
import com.example.gymapp.data.remote.api.RutinaApi
import com.example.gymapp.data.repository.AuthRepositoryImpl
import com.example.gymapp.data.repository.RutinaRepositoryImpl
import com.example.gymapp.domain.repository.AuthRepository
import com.example.gymapp.domain.repository.RutinaRepository
import com.example.gymapp.domain.repository.SessionStorage
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class AppContainer(context: Context) {

    // 10.0.2.2 es el "localhost" de tu computadora visto desde el emulador
    private val baseUrl = "http://10.0.2.2:8000/"

    val sessionStorage: SessionStorage = SharedPrefsSessionStorage(context)

    private val json = Json { ignoreUnknownKeys = true }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(sessionStorage))
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    private val rutinaApi: RutinaApi = retrofit.create(RutinaApi::class.java)

    val authRepository: AuthRepository = AuthRepositoryImpl(authApi, sessionStorage)
    val rutinaRepository: RutinaRepository = RutinaRepositoryImpl(rutinaApi)
}