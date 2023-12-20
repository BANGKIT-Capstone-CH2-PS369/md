package com.example.pindairasa.API

import com.example.pindairasa.response.LoginResponse
import com.example.pindairasa.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}