package com.example.a213396_lingchinwei_drnazatulaini_lab4.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue
import com.example.a213396_lingchinwei_drnazatulaini_lab4.BottomNavigationBar
import com.example.a213396_lingchinwei_drnazatulaini_lab4.AtlasScreen
import com.example.a213396_lingchinwei_drnazatulaini_lab4.ui.theme.*

@Composable
fun HubScreen(navController: NavHostController) {
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
                Text("Study Hub", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HubResourceCard("Research",     "Browse curated academic papers and references for your subjects.",        Icons.Outlined.ContentPasteSearch, AtlasSky)
                HubResourceCard("Practice Tests","Test your knowledge with past exam questions and timed quizzes.",       Icons.Outlined.AssignmentTurnedIn, AccentGold)
                HubResourceCard("Flashcards",   "Memorise key concepts with spaced-repetition flashcard decks.",          Icons.Outlined.Style,              AccentPurple)
                HubResourceCard("PDF Reader",   "Access your course materials and annotate documents on the go.",         Icons.Outlined.PictureAsPdf,       AccentGreen)
                HubResourceCard("Mentors",      "Connect with subject mentors and book live tutoring sessions.",          Icons.Outlined.Groups,             AtlasBlue)
                HubResourceCard("Atlas AI",     "Get instant AI-powered explanations and study recommendations.",         Icons.Outlined.AutoAwesome,        AtlasSky)
            }
        }
    }
}

@Composable
private fun HubResourceCard(title: String, description: String, icon: ImageVector, accent: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(accent.copy(alpha = 0.12f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(26.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AtlasNavy)
                Spacer(modifier = Modifier.height(4.dp))
                Text(description, fontSize = 13.sp, color = AtlasNavy.copy(alpha = 0.6f), lineHeight = 18.sp)
            }
        }
    }
}
