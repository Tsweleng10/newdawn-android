package com.example.newdawn.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object FindWork : Screen("findWork")
    object PostJob : Screen("postJob")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    // Gents add di: JobDetails, SubmitOffer, MyJobs, ViewOffers, mehlolhlol.
}