package com.example.pindairasa.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pindairasa.API.LoginRequest
import com.example.pindairasa.pref.UserModel
import com.example.pindairasa.repository.repo
import com.example.pindairasa.response.ErrorResponse
import com.example.pindairasa.response.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: repo) : ViewModel() {

    private fun saveSession(user: UserModel){
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun loginUser(email: String, password: String, onLoginComplete: (LoginResponse) -> Unit){
        viewModelScope.launch {
            try {
                val response = repository.login(loginRequest = LoginRequest(email, password))
                val message = response.msg
                onLoginComplete(LoginResponse(error = false, msg = message))

                val token = response.loginResult?.token
                if (token!=null){
                    saveSession(UserModel(email, token, true))
                }
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                onLoginComplete(LoginResponse(error = true, msg = errorMessage))
                if (errorMessage != null) {
                    Log.e("LoginViewModel", errorMessage)
                }
            } catch (e: Exception) {
                val errorMessage = "Gagal masuk: ${e.message}"
                onLoginComplete(LoginResponse(error = true, msg = errorMessage))
                Log.e("LoginViewModel", errorMessage)
            }
        }
    }
}