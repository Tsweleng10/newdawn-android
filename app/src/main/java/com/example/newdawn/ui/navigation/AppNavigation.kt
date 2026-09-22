package com.example.newdawn.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        composable(Screen.Splash.route) {
            Text("Splash Screen")
        }
        composable(Screen.Login.route) {
            Text("Login Screen")
        }
        composable(Screen.Register.route) {
            Text("Register Screen")
        }
        composable(Screen.Home.route) {
            Text("Home Screen")
        }
        composable(Screen.FindWork.route) {
            Text("Find Work Screen")
        }
        composable(Screen.PostJob.route) {
            Text("Post Job Screen")
        }
        composable(Screen.Profile.route) {
            Text("Profile Screen")
        }
        composable(Screen.Settings.route) {
            Text("Settings Screen")
        }
    }
}