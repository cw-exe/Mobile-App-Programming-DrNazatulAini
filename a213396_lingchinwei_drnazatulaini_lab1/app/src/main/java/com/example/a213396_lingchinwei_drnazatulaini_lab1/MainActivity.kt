package com.example.a213396_lingchinwei_drnazatulaini_lab1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a213396_lingchinwei_drnazatulaini_lab1.ui.theme.A213396_lingchinwei_drnazatulaini_lab1Theme

// New Nordic Slate & Gold Theme Colors
val NordicSlate = Color(0xFF1A1D29)
val NordicGold = Color(0xFFFBBF24)
val NordicCard = Color(0xFF262A3B)
val NordicText = Color(0xFFE2E8F0)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A213396_lingchinwei_drnazatulaini_lab1Theme {
                StudyAppMainScreen()
            }
        }
    }
}

@Composable
fun StudyAppMainScreen() {
    Scaffold(
        bottomBar = { BottomNavigationBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF1F5F9)) // Overall page background
        ) {
            // --- Header Section ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(NordicSlate)
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "AtlasLearn",
                        style = TextStyle(
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                    Icon(
                        Icons.Outlined.Settings,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // --- Floating Action Hub ---
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .offset(y = (-50).dp) // Higher offset for stronger overlap
                    .shadow(12.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                // Action Grid Rework (Row of 3, Row of 3)
                Text("STUDY HUB", fontWeight = FontWeight.Black, fontSize = 12.sp, color = NordicSlate.copy(alpha = 0.5f), letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    ActionIconItem("Research", Icons.Outlined.ContentPasteSearch)
                    ActionIconItem("Practices", Icons.Outlined.AssignmentTurnedIn)
                    ActionIconItem("Flashcards", Icons.Outlined.Style)
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    ActionIconItem("PDF Reader", Icons.Outlined.PictureAsPdf)
                    ActionIconItem("Mentors", Icons.Outlined.Groups)
                    // Mimi AI replaced by Atlas AI with different icon/color
                    ActionIconItem("Atlas AI", Icons.Outlined.AutoAwesome, tint = NordicGold)
                }
            }

            // --- Subject List Reworked into Categories ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-30).dp) // Adjusted offset
                    .background(NordicSlate) // The actual list background texture
                    .padding(horizontal = 20.dp, vertical = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Curriculum Browse",
                        style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = NordicGold)
                    )
                    Icon(Icons.Default.FilterList, contentDescription = null, tint = NordicText, modifier = Modifier.size(24.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // CATEGORY: Mathematics
                CategorySection("Mathematics", Icons.Default.Calculate) {
                    SubjectCard("Advanced Physics", NordicGold, Icons.Default.Functions)
                    SubjectCard("Calculus I", NordicGold, Icons.Default.Functions)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // CATEGORY: Languages
                CategorySection("Languages", Icons.Default.Translate) {
                    SubjectCard("Bahasa Arab", Color(0xFFC084FC), Icons.Default.Book)
                    SubjectCard("Bahasa Melayu", Color(0xFFC084FC), Icons.Default.Spellcheck)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // CATEGORY: Sciences
                CategorySection("Sciences", Icons.Default.Science) {
                    SubjectCard("Chemistry", Color(0xFF4ADE80), Icons.Default.Biotech)
                    SubjectCard("Biologi", Color(0xFF4ADE80), Icons.Default.Hive)
                    SubjectCard("Biology", Color(0xFF4ADE80), Icons.Default.NaturePeople)
                }

                // Extra spacer for scroll clearance
                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }
}

@Composable
fun CategorySection(title: String, icon: ImageVector, content: @Composable RowScope.() -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = NordicText.copy(alpha = 0.7f), modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = NordicText)
        }
        Spacer(modifier = Modifier.height(12.dp))
        // Reworked from a vertical list to a horizontal scroll
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            content()
        }
    }
}

@Composable
fun SubjectCard(title: String, iconColor: Color, icon: ImageVector) {
    Box(
        modifier = Modifier
            .width(180.dp) // Fixed width for horizontal scroll
            .height(100.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(NordicCard) // New Card Color
            .padding(16.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Icon(
                icon,
                contentDescription = null,
                tint = iconColor, // Use unique icon/color
                modifier = Modifier.size(28.dp).offset(x = (-4).dp) // Subtle neomorphic placement
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(text = title, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = NordicText)
        }
    }
}

@Composable
fun ActionIconItem(label: String, icon: ImageVector, tint: Color = NordicSlate) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Icon changed from outlined to filled/custom neomorphic look
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(32.dp))
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar(containerColor = NordicSlate, tonalElevation = 10.dp) {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
            label = { Text("Overview") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = NordicGold, selectedTextColor = NordicGold, indicatorColor = NordicCard)
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Hub, contentDescription = null) },
            label = { Text("Hub") },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = NordicText, unselectedTextColor = NordicText)
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Outlined.EmojiEvents, contentDescription = null) },
            label = { Text("Awards") },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = NordicText, unselectedTextColor = NordicText)
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Outlined.PersonSearch, contentDescription = null) },
            label = { Text("Profile") },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = NordicText, unselectedTextColor = NordicText)
        )
    }
}