package com.example.pindairasa.API

import com.example.pindairasa.response.LoginResponse
import com.example.pindairasa.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}