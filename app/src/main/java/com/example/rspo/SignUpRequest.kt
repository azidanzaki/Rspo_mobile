package com.example.rspo

data class SignUpRequest(
    val username: String,
    val email: String,
    val password: String,
    val confirm_password: String
)
