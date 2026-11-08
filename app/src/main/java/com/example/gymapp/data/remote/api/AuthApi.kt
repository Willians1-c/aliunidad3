package com.example.gymapp.data.remote.api



import com.example.gymapp.data.remote.dto.AuthResponseDto
import com.example.gymapp.data.remote.dto.LoginRequestDto
import com.example.gymapp.data.remote.dto.RegisterRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("register")
    suspend fun register(@Body body: RegisterRequestDto): AuthResponseDto

    @POST("login")
    suspend fun login(@Body body: LoginRequestDto): AuthResponseDto
}