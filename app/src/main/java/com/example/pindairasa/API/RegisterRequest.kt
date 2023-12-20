package com.example.pindairasa.API

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val confirmpassword: String
)