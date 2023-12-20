package com.example.pindairasa.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pindairasa.API.RegisterRequest
import com.example.pindairasa.repository.repo
import com.example.pindairasa.response.ErrorResponse
import com.example.pindairasa.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val repository: repo) : ViewModel() {
    fun registerUser(name: String, email: String, password: String, repassword: String, onRegisterComplete: (RegisterResponse) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.registerUser(registerRequest = RegisterRequest(name, email, password, repassword))
                val message = response.message
                onRegisterComplete(RegisterResponse(error = false, message = message))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                onRegisterComplete(RegisterResponse(error = true, message = errorMessage))
                if (errorMessage != null) {
                    Log.e("SignupViewModel", errorMessage)
                }
            } catch (e: Exception) {
                val errorMessage = "Gagal mendaftarkan pengguna: ${e.message}"
                onRegisterComplete(RegisterResponse(error = true, message = errorMessage))
                Log.e("SignupViewModel", errorMessage)
            }
        }
    }
}