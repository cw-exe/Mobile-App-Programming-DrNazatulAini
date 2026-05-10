package com.example.a213396_lingchinwei_drnazatulaini_project1

// MainActivity is the entry point of the app.
// It launches AtlasApp which handles all navigation and screens.

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AppTheme
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AtlasCard
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AtlasNavy
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AtlasSky
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AtlasText

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                AtlasApp()
            }
        }
    }
}

// The bottom navigation bar shown at the bottom of every screen
@Composable
fun BottomNavigationBar(navController: NavHostController, currentRoute: String?) {
    NavigationBar(containerColor = AtlasNavy, tonalElevation = 10.dp) {

        // Home tab
        NavigationBarItem(
            selected = currentRoute == AtlasScreen.Home.name,
            onClick = {
                navController.navigate(AtlasScreen.Home.name) {
                    popUpTo(AtlasScreen.Home.name) { inclusive = false }
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Home") },
            colors = navBarItemColors()
        )

        // Ask tab — for submitting help requests
        NavigationBarItem(
            selected = currentRoute == AtlasScreen.Ask.name,
            onClick = {
                navController.navigate(AtlasScreen.Ask.name) {
                    popUpTo(AtlasScreen.Home.name) { inclusive = false }
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.Help, contentDescription = null) },
            label = { Text("Ask") },
            colors = navBarItemColors()
        )

        // Sessions tab — shows the list of submitted help requests
        NavigationBarItem(
            selected = currentRoute == AtlasScreen.Sessions.name,
            onClick = {
                navController.navigate(AtlasScreen.Sessions.name) {
                    popUpTo(AtlasScreen.Home.name) { inclusive = false }
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.List, contentDescription = null) },
            label = { Text("Requests") },
            colors = navBarItemColors()
        )

        // Match tab — browse volunteer mentors
        NavigationBarItem(
            selected = currentRoute == AtlasScreen.Match.name,
            onClick = {
                navController.navigate(AtlasScreen.Match.name) {
                    popUpTo(AtlasScreen.Home.name) { inclusive = false }
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.People, contentDescription = null) },
            label = { Text("Mentors") },
            colors = navBarItemColors()
        )

        // Profile tab
        NavigationBarItem(
            selected = currentRoute == AtlasScreen.Profile.name,
            onClick = {
                navController.navigate(AtlasScreen.Profile.name) {
                    popUpTo(AtlasScreen.Home.name) { inclusive = false }
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Profile") },
            colors = navBarItemColors()
        )
    }
}

// Helper that returns the same color settings for every nav bar item
@Composable
fun navBarItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor   = AtlasSky,
    selectedTextColor   = AtlasSky,
    indicatorColor      = AtlasCard,
    unselectedIconColor = AtlasText,
    unselectedTextColor = AtlasText
)
