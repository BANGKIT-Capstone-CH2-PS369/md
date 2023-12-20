package com.example.pindairasa.repository

import com.example.pindairasa.API.ApiService
import com.example.pindairasa.API.LoginRequest
import com.example.pindairasa.API.RegisterRequest
import com.example.pindairasa.pref.UserModel
import com.example.pindairasa.pref.UserPreference
import com.example.pindairasa.response.LoginResponse
import com.example.pindairasa.response.RegisterResponse
import kotlinx.coroutines.flow.Flow

class repo private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
){
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun registerUser(registerRequest: RegisterRequest): RegisterResponse {
        return apiService.register(registerRequest)
    }

    suspend fun login(loginRequest:LoginRequest): LoginResponse {
        return apiService.login(loginRequest)
    }

    companion object {
        @Volatile
        private var instance: repo? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): repo =
            instance ?: synchronized(this) {
                instance ?: repo(apiService, userPreference)
            }.also { instance = it }
    }
}