package com.mahmood.callmate.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mahmood.callmate.presentation.states.ContactStates
import com.mahmood.callmate.presentation.ui_layer.AddEditScreen
import com.mahmood.callmate.presentation.ui_layer.DetailScreen
import com.mahmood.callmate.presentation.ui_layer.HomeScreen
import com.mahmood.callmate.presentation.ui_layer.Splash
import com.mahmood.callmate.presentation.viewModel.ContactViewModel

@Composable
fun NavGraph(navController: NavHostController, viewModel: ContactViewModel,modifier: Modifier = Modifier) {

    val state = viewModel.state.collectAsState().value

    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
        composable(Screen.SplashScreen.route) {
            Splash(navHostController = navController)
        }
        composable(Screen.HomeScreen.route) {
            HomeScreen(viewModel = viewModel, navController = navController, state = state)
        }
        composable(Screen.AddContact.route) {
            AddEditScreen(
                state = state,
                navController = navController,
                onEvent = { viewModel.saveContact() })
        }
        composable(
            Screen.DetailScreen.route
        ) {
            DetailScreen(viewModel = viewModel, navController = navController, state = state)
        }

    }
}

