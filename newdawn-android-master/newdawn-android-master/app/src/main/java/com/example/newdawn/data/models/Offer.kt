package com.example.newdawn.data.models

data class Offer(
    val id: Int = 0,
    val job_id: Int,
    val worker_id: Int = 0,
    val proposed_price: Double,
    val message: String,
    val availability: String,
    val status: String = "PENDING",
    val worker_name: String? = null,
    val created_at: String? = null
)