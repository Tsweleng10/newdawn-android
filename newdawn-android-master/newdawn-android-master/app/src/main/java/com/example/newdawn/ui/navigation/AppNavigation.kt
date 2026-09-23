package com.example.newdawn.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newdawn.ui.auth.SplashScreen

// Person 3 imports
import com.example.newdawn.ui.home.HomeScreen
import com.example.newdawn.ui.jobs.FindWorkScreen
import com.example.newdawn.ui.jobs.JobDetailsScreen
import com.example.newdawn.ui.jobs.PostJobScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        // Splash
        composable(Screen.Splash.route) { ... }
        // Login
        composable(Screen.Login.route) { Text("Login Screen") }
        // Register
        composable(Screen.Register.route) { Text("Register Screen") }
        // Home
        composable(Screen.Home.route) { HomeScreen(navController = navController) }
        // Find Work
        composable(Screen.FindWork.route) { FindWorkScreen(navController = navController) }
        // Job Details
        composable(
            route = Screen.JobDetails.route,
            arguments = listOf(navArgument("jobId") { type = NavType.IntType })
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getInt("jobId") ?: 0
            JobDetailsScreen(jobId = jobId, navController = navController)
        }
        // Post Job
        composable(Screen.PostJob.route) { PostJobScreen(navController = navController) }

        // --- REMAINING PLACEHOLDERS ---
        // Submit Offer
        composable(...) { ... Text("Submit Offer for job #$jobId ($jobTitle)") }
        // My Jobs
        composable(Screen.MyJobs.route) { Text("My Jobs Screen") }
        // View Offers
        composable(...) { ... Text("View Offers for job #$jobId") }
        // Job Progress
        composable(...) { ... Text("Job Progress for job #$jobId") }
        // Rate Worker
        composable(...) { ... Text("Rate worker #$workerId for job #$jobId") }
        // Profile
        composable(Screen.Profile.route) { Text("Profile Screen") }
        // Settings
        composable(Screen.Settings.route) { Text("Settings Screen") }
    }
}