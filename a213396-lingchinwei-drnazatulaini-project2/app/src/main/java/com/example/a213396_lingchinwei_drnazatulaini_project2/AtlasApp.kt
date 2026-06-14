package com.example.a213396_lingchinwei_drnazatulaini_project2

// AtlasApp wires together every screen via Jetpack Navigation.
// One shared ViewModel (AtlasViewModel) is passed to all screens so they
// all read from the same Room and Firestore streams.

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.a213396_lingchinwei_drnazatulaini_project2.ui.AppViewModelProvider
import com.example.a213396_lingchinwei_drnazatulaini_project2.ui.AtlasViewModel
import com.example.a213396_lingchinwei_drnazatulaini_project2.ui.AskScreen
import com.example.a213396_lingchinwei_drnazatulaini_project2.ui.CommunityBoardScreen
import com.example.a213396_lingchinwei_drnazatulaini_project2.ui.HomeScreen
import com.example.a213396_lingchinwei_drnazatulaini_project2.ui.MatchScreen
import com.example.a213396_lingchinwei_drnazatulaini_project2.ui.MyResponsesScreen
import com.example.a213396_lingchinwei_drnazatulaini_project2.ui.ProfileScreen
import com.example.a213396_lingchinwei_drnazatulaini_project2.ui.RequestDetailScreen
import com.example.a213396_lingchinwei_drnazatulaini_project2.ui.SessionsScreen

// Navigation destinations.
// CommunityBoard and MyResponses are sub-screens (no bottom nav); they are
// reached via buttons on the Sessions screen and navigated back with popBackStack().
// RequestDetail uses a string argument so its route is a plain string literal below.
enum class AtlasScreen { Home, Ask, Sessions, Match, Profile, CommunityBoard, MyResponses }

@Composable
fun AtlasApp(atlasViewModel: AtlasViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val navController = rememberNavController()

    NavHost(
        navController    = navController,
        startDestination = AtlasScreen.Home.name,
        enterTransition  = { fadeIn(tween(260))  + scaleIn(tween(260),  initialScale = 0.96f) },
        exitTransition   = { fadeOut(tween(180)) + scaleOut(tween(180), targetScale  = 1.04f) },
        popEnterTransition  = { fadeIn(tween(260))  + scaleIn(tween(260),  initialScale = 0.96f) },
        popExitTransition   = { fadeOut(tween(180)) + scaleOut(tween(180), targetScale  = 1.04f) }
    ) {
        // ── Main tabs ──────────────────────────────────────────────────────────
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

        // ── Community sub-screens ──────────────────────────────────────────────
        composable(AtlasScreen.CommunityBoard.name) {
            CommunityBoardScreen(navController = navController, viewModel = atlasViewModel)
        }
        composable(AtlasScreen.MyResponses.name) {
            MyResponsesScreen(navController = navController, viewModel = atlasViewModel)
        }

        // RequestDetail uses a string argument (the Firestore document ID).
        // This mirrors the Lab 4 argument-passing pattern:
        //   navController.navigate("RequestDetail/$requestId")   ← sender
        //   backStackEntry.arguments?.getString("requestId")     ← receiver
        composable(
            route     = "RequestDetail/{requestId}",
            arguments = listOf(navArgument("requestId") { type = NavType.StringType })
        ) { backStackEntry ->
            val requestId = backStackEntry.arguments?.getString("requestId") ?: ""
            RequestDetailScreen(
                requestId     = requestId,
                navController = navController,
                viewModel     = atlasViewModel
            )
        }
    }
}
