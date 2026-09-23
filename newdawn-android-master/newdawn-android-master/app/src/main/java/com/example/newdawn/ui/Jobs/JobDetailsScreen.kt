package com.example.newdawn.ui.jobs

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newdawn.data.models.Job
import com.example.newdawn.data.remote.RetrofitInstance
import com.example.newdawn.ui.navigation.Screen
import com.example.newdawn.utils.TokenManager

@Composable
fun JobDetailsScreen(
    jobId: Int,
    navController: NavController
) {
    val context = LocalContext.current
    // Shared DataStore instance, so creating this here is safe and cheap
    val tokenManager = remember { TokenManager(context.applicationContext) }

    var job by remember { mutableStateOf<Job?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var reloadKey by remember { mutableStateOf(0) }

    LaunchedEffect(jobId, reloadKey) {
        Log.d("JobsScreen", "Loading details for job #$jobId")
        isLoading = true
        errorMessage = null
        try {
            val authHeader = tokenManager.getAuthHeader()
            if (authHeader.isEmpty()) {
                errorMessage = "You are not logged in. Please log in again."
                isLoading = false
                return@LaunchedEffect
            }

            val response = RetrofitInstance.api.getJobById(authHeader, jobId)
            if (response.isSuccessful) {
                job = response.body()
                Log.d("JobsScreen", "Loaded job #$jobId: ${job?.title}")
            } else {
                Log.e("JobsScreen", "getJobById($jobId) failed: HTTP ${response.code()}")
                errorMessage = "Could not load job (HTTP ${response.code()})"
            }
        } catch (e: Exception) {
            Log.e("JobsScreen", "getJobById network error", e)
            errorMessage = "Network error: ${e.message ?: "unknown"}"
        }
        isLoading = false
    }

    Box(Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            errorMessage != null -> {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(12.dp))
                    OutlinedButton(onClick = { reloadKey++ }) { Text("Retry") }
                }
            }

            job == null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Job not found.")
                }
            }

            else -> {
                val currentJob = job!!
                val isOpen = currentJob.status.equals("OPEN", ignoreCase = true)

                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp)
                ) {
                    Text(
                        text = currentJob.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "${currentJob.category} - ${currentJob.location}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Status: ${currentJob.status}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "R ${currentJob.budget}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Posted by ${currentJob.poster_name}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = currentJob.description,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(Modifier.height(32.dp))

                    // Offer submission is owned by Person 4 - we just hand off jobId + title
                    Button(
                        onClick = {
                            Log.d("JobsScreen", "SUBMIT OFFER tapped for job #${currentJob.id}")
                            navController.navigate(
                                Screen.SubmitOffer.createRoute(
                                    jobId = currentJob.id,
                                    jobTitle = currentJob.title
                                )
                            )
                        },
                        enabled = isOpen,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text(if (isOpen) "SUBMIT OFFER" else "JOB NO LONGER OPEN")
                    }

                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}