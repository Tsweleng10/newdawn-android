package com.example.newdawn.data.models

data class Job(
    val id: Int = 0,
    val poster_id: Int = 0,
    val title: String,
    val category: String,
    val description: String,
    val location: String,
    val budget: Double,
    val status: String = "OPEN",
    val poster_name: String? = null,
    val created_at: String? = null
)