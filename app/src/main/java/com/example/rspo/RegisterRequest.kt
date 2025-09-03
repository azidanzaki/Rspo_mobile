package com.example.rspo

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val confirm_password: String
)
