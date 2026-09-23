package com.example.newdawn.data.models

data class RegisterRequest(
    val full_name: String,
    val email: String,
    val password: String,
    val location: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val user: User,
    val token: String
)