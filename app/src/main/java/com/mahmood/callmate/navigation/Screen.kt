package com.mahmood.callmate.navigation

sealed class Screen(val route: String) {

    object SplashScreen : Screen("splash_screen")
    object HomeScreen: Screen("home_screen")
    object AddContact : Screen("add_contact")
    object DetailScreen : Screen("detail_screen")
}