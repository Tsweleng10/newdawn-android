package com.example.newdawn.data.models

data class User(
    val id: Int,
    val full_name: String,
    val email: String,
    val location: String? = null
)