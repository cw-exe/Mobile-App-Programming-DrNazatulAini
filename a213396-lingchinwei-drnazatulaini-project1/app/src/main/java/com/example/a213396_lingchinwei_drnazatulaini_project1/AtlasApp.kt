package com.example.a213396_lingchinwei_drnazatulaini_project1

// This file sets up navigation for the whole app.
// AtlasApp creates one shared ViewModel and passes it to all 5 screens.

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.AtlasViewModel
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.AskScreen
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.HomeScreen
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.MatchScreen
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.ProfileScreen
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.SessionsScreen

// The names of all 5 screens used for navigation routes
enum class AtlasScreen { Home, Ask, Sessions, Match, Profile }

// This is the root of the app — it creates one ViewModel and shares it with every screen
@Composable
fun AtlasApp(atlasViewModel: AtlasViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(
        navController    = navController,
        startDestination = AtlasScreen.Home.name,
        enterTransition  = { fadeIn(tween(260)) + scaleIn(tween(260), initialScale = 0.96f) },
        exitTransition   = { fadeOut(tween(180)) + scaleOut(tween(180), targetScale = 1.04f) },
        popEnterTransition  = { fadeIn(tween(260)) + scaleIn(tween(260), initialScale = 0.96f) },
        popExitTransition   = { fadeOut(tween(180)) + scaleOut(tween(180), targetScale = 1.04f) }
    ) {
        composable(AtlasScreen.Home.name) {
            HomeScreen(navController = navController, viewModel = atlasViewModel)
        }
        composable(AtlasScreen.Ask.name) {
            AskScreen(navController = navController, viewModel = atlasViewModel)
        }
        composable(AtlasScreen.Sessions.name) {
            SessionsScreen(navController = navController, viewModel = atlasViewModel)
        }
        composable(AtlasScreen.Match.name) {
            MatchScreen(navController = navController, viewModel = atlasViewModel)
        }
        composable(AtlasScreen.Profile.name) {
            ProfileScreen(navController = navController, viewModel = atlasViewModel)
        }
    }
}
