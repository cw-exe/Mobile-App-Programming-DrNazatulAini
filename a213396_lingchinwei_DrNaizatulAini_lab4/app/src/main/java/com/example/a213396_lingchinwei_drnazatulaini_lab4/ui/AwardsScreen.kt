package com.example.a213396_lingchinwei_drnazatulaini_lab4.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.a213396_lingchinwei_drnazatulaini_lab4.BottomNavigationBar
import com.example.a213396_lingchinwei_drnazatulaini_lab4.ui.theme.*

private data class Award(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val unlocked: Boolean
)

private val awards = listOf(
    Award("First Login",    "Logged in for the first time",        Icons.Default.Login,        AtlasSky,              true),
    Award("Quick Learner",  "Completed 5 study sessions",          Icons.Default.Bolt,         AccentGold,            true),
    Award("Subject Star",   "Searched for 3 subjects",             Icons.Default.Star,         AccentGold,            true),
    Award("Card Collector", "Expanded every subject card",         Icons.Default.Style,        AccentPurple,          false),
    Award("Study Streak",   "Studied 7 days in a row",             Icons.Default.Whatshot,    Color(0xFFFF7043),     false),
    Award("Top Scholar",    "Completed all practice tests",        Icons.Default.EmojiEvents,  AccentGold,            false),
)

@Composable
fun AwardsScreen(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, currentRoute) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF0F4FF))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(AtlasNavy)
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text("Awards", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            }

            Text(
                "${awards.count { it.unlocked }} / ${awards.size} unlocked",
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                fontSize = 14.sp,
                color = AtlasNavy.copy(alpha = 0.6f),
                fontWeight = FontWeight.SemiBold
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(awards) { award -> AwardCard(award) }
            }
        }
    }
}

@Composable
private fun AwardCard(award: Award) {
    val alpha = if (award.unlocked) 1f else 0.35f
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = if (award.unlocked) 3.dp else 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(award.color.copy(alpha = 0.15f * alpha), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(award.icon, contentDescription = null, tint = award.color.copy(alpha = alpha), modifier = Modifier.size(30.dp))
            }
            Text(award.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AtlasNavy.copy(alpha = alpha), textAlign = TextAlign.Center)
            Text(award.description, fontSize = 11.sp, color = AtlasNavy.copy(alpha = 0.5f * alpha), textAlign = TextAlign.Center, lineHeight = 15.sp)
            if (!award.unlocked) {
                Icon(Icons.Default.Lock, contentDescription = "Locked", tint = AtlasNavy.copy(alpha = 0.3f), modifier = Modifier.size(16.dp))
            }
        }
    }
}
