package com.example.a213396_lingchinwei_drnazatulaini_lab4

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a213396_lingchinwei_drnazatulaini_lab4.ui.AwardsScreen
import com.example.a213396_lingchinwei_drnazatulaini_lab4.ui.HubScreen
import com.example.a213396_lingchinwei_drnazatulaini_lab4.ui.ProfileScreen
import com.example.a213396_lingchinwei_drnazatulaini_lab4.ui.ProfileViewModel

enum class AtlasScreen { Home, Hub, Awards, Profile }

@Composable
fun AtlasApp(viewModel: ProfileViewModel = viewModel()) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()

    NavHost(navController = navController, startDestination = AtlasScreen.Home.name) {
        composable(AtlasScreen.Home.name) {
            StudyAppMainScreen(navController)
        }
        composable(AtlasScreen.Hub.name) {
            HubScreen(navController)
        }
        composable(AtlasScreen.Awards.name) {
            AwardsScreen(navController)
        }
        composable(AtlasScreen.Profile.name) {
            ProfileScreen(
                navController = navController,
                uiState       = uiState,
                onNameChange  = viewModel::updateStudentName,
                onIdChange    = viewModel::updateStudentId
            )
        }
    }
}
