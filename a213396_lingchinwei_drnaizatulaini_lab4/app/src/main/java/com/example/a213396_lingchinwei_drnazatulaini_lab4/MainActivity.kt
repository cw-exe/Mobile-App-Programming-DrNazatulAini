package com.example.a213396_lingchinwei_drnazatulaini_lab4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.a213396_lingchinwei_drnazatulaini_lab4.ui.theme.AppTheme
import com.example.a213396_lingchinwei_drnazatulaini_lab4.ui.theme.AccentGold
import com.example.a213396_lingchinwei_drnazatulaini_lab4.ui.theme.AccentPurple
import com.example.a213396_lingchinwei_drnazatulaini_lab4.ui.theme.AccentGreen
import com.example.a213396_lingchinwei_drnazatulaini_lab4.ui.theme.AtlasNavy
import com.example.a213396_lingchinwei_drnazatulaini_lab4.ui.theme.AtlasBlue
import com.example.a213396_lingchinwei_drnazatulaini_lab4.ui.theme.AtlasSky
import com.example.a213396_lingchinwei_drnazatulaini_lab4.ui.theme.AtlasText
import com.example.a213396_lingchinwei_drnazatulaini_lab4.ui.theme.AtlasCard

data class Subject(
    val name: String,
    val accent: Color,
    val icon: ImageVector,
    val description: String
)

val allSubjects = listOf(
    Subject("Advanced Physics", AccentGold,   Icons.Default.Functions,
        "Mechanics, Electromagnetism, Quantum & Thermodynamics."),
    Subject("Calculus I",       AccentGold,   Icons.Default.Functions,
        "Limits, derivatives, integrals, and the Fundamental Theorem."),
    Subject("Bahasa Arab",      AccentPurple, Icons.Default.Book,
        "Grammar, vocabulary, reading and writing in Arabic."),
    Subject("Bahasa Melayu",    AccentPurple, Icons.Default.Spellcheck,
        "Malay language proficiency, essays and comprehension."),
    Subject("Chemistry",        AccentGreen,  Icons.Default.Biotech,
        "Atomic structure, bonding, reactions and stoichiometry."),
    Subject("Biologi",          AccentGreen,  Icons.Default.Hive,
        "Sel, genetik, ekologi dan sistem badan manusia."),
    Subject("Biology",          AccentGreen,  Icons.Default.NaturePeople,
        "Cell biology, genetics, ecology and human physiology.")
)

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

@Composable
fun StudyAppMainScreen(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    var searchQuery    by remember { mutableStateOf("") }
    var submittedQuery by remember { mutableStateOf("") }

    val filteredSubjects = remember(submittedQuery) {
        if (submittedQuery.isBlank()) allSubjects
        else allSubjects.filter { it.name.contains(submittedQuery, ignoreCase = true) }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, currentRoute) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF0F4FF))
        ) {
            // ── Header ────────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(AtlasNavy)
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
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

            // ── Study Hub Card ────────────────────────────────────────────────
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .offset(y = (-50).dp)
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        "STUDY HUB",
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        color = AtlasNavy.copy(alpha = 0.5f),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ActionIconItem("Research",  Icons.Outlined.ContentPasteSearch)
                        ActionIconItem("Practices", Icons.Outlined.AssignmentTurnedIn)
                        ActionIconItem("Flashcards",Icons.Outlined.Style)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ActionIconItem("PDF Reader", Icons.Outlined.PictureAsPdf)
                        ActionIconItem("Mentors",    Icons.Outlined.Groups)
                        ActionIconItem("Atlas AI",   Icons.Outlined.AutoAwesome, tint = AtlasSky)
                    }
                }
            }

            // ── Curriculum Browse Section ─────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .offset(y = (-30).dp)
                    .background(AtlasNavy)
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
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AtlasSky
                        )
                    )
                    Icon(
                        Icons.Default.FilterList,
                        contentDescription = null,
                        tint = AtlasText,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AtlasCard)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text(
                                    "Search subject...",
                                    color = AtlasText.copy(alpha = 0.4f),
                                    fontSize = 14.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = null,
                                    tint = AtlasSky
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor   = AtlasSky,
                                unfocusedBorderColor = AtlasText.copy(alpha = 0.3f),
                                focusedTextColor     = AtlasText,
                                unfocusedTextColor   = AtlasText,
                                cursorColor          = AtlasSky,
                                focusedContainerColor   = AtlasCard,
                                unfocusedContainerColor = AtlasCard
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Button(
                            onClick = { submittedQuery = searchQuery },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AtlasBlue),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (submittedQuery.isNotBlank()) {
                    val count   = filteredSubjects.size
                    val message = if (count > 0)
                        "Showing $count result(s) for \"$submittedQuery\""
                    else
                        "No subjects found for \"$submittedQuery\""
                    Text(
                        text = message,
                        color = if (count > 0) AtlasSky else Color(0xFFFC8181),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                if (filteredSubjects.isNotEmpty()) {
                    val mathSubjects = filteredSubjects.filter { it.accent == AccentGold }
                    val langSubjects = filteredSubjects.filter { it.accent == AccentPurple }
                    val sciSubjects  = filteredSubjects.filter { it.accent == AccentGreen }

                    if (mathSubjects.isNotEmpty()) {
                        CategorySection("Mathematics", Icons.Default.Calculate) {
                            mathSubjects.forEach { ExpandableSubjectCard(it) }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    if (langSubjects.isNotEmpty()) {
                        CategorySection("Languages", Icons.Default.Translate) {
                            langSubjects.forEach { ExpandableSubjectCard(it) }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    if (sciSubjects.isNotEmpty()) {
                        CategorySection("Sciences", Icons.Default.Science) {
                            sciSubjects.forEach { ExpandableSubjectCard(it) }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }
}

// ── Category Section ──────────────────────────────────────────────────────────
@Composable
fun CategorySection(title: String, icon: ImageVector, content: @Composable RowScope.() -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                icon,
                contentDescription = null,
                tint = AtlasText.copy(alpha = 0.7f),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = AtlasText)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            content()
        }
    }
}

// ── Expandable Subject Card ───────────────────────────────────────────────────
@Composable
fun ExpandableSubjectCard(subject: Subject) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .width(180.dp)
            .clickable { expanded = !expanded }
            .animateContentSize(animationSpec = tween(durationMillis = 300)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AtlasCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    subject.icon,
                    contentDescription = null,
                    tint = subject.accent,
                    modifier = Modifier.size(28.dp)
                )
                Icon(
                    if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = AtlasText.copy(alpha = 0.5f),
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = subject.name,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = AtlasText
            )

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(animationSpec = tween(300)),
                exit  = shrinkVertically(animationSpec = tween(300))
            ) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(
                        color = subject.accent.copy(alpha = 0.3f),
                        thickness = 1.dp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = subject.description,
                        fontSize = 12.sp,
                        color = AtlasText.copy(alpha = 0.75f),
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

// ── Action Icon ───────────────────────────────────────────────────────────────
@Composable
fun ActionIconItem(label: String, icon: ImageVector, tint: Color = AtlasNavy) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}

// ── Bottom Navigation ─────────────────────────────────────────────────────────
@Composable
fun BottomNavigationBar(navController: NavHostController, currentRoute: String?) {
    NavigationBar(containerColor = AtlasNavy, tonalElevation = 10.dp) {
        NavigationBarItem(
            selected = currentRoute == AtlasScreen.Home.name,
            onClick  = {
                navController.navigate(AtlasScreen.Home.name) {
                    popUpTo(AtlasScreen.Home.name) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon     = { Icon(Icons.Default.Dashboard, contentDescription = null) },
            label    = { Text("Overview") },
            colors   = NavigationBarItemDefaults.colors(
                selectedIconColor   = AtlasSky,
                selectedTextColor   = AtlasSky,
                indicatorColor      = AtlasCard,
                unselectedIconColor = AtlasText,
                unselectedTextColor = AtlasText
            )
        )
        NavigationBarItem(
            selected = currentRoute == AtlasScreen.Hub.name,
            onClick  = {
                navController.navigate(AtlasScreen.Hub.name) {
                    popUpTo(AtlasScreen.Home.name) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon     = { Icon(Icons.Default.Hub, contentDescription = null) },
            label    = { Text("Hub") },
            colors   = NavigationBarItemDefaults.colors(
                selectedIconColor   = AtlasSky,
                selectedTextColor   = AtlasSky,
                indicatorColor      = AtlasCard,
                unselectedIconColor = AtlasText,
                unselectedTextColor = AtlasText
            )
        )
        NavigationBarItem(
            selected = currentRoute == AtlasScreen.Awards.name,
            onClick  = {
                navController.navigate(AtlasScreen.Awards.name) {
                    popUpTo(AtlasScreen.Home.name) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon     = { Icon(Icons.Outlined.EmojiEvents, contentDescription = null) },
            label    = { Text("Awards") },
            colors   = NavigationBarItemDefaults.colors(
                selectedIconColor   = AtlasSky,
                selectedTextColor   = AtlasSky,
                indicatorColor      = AtlasCard,
                unselectedIconColor = AtlasText,
                unselectedTextColor = AtlasText
            )
        )
        NavigationBarItem(
            selected = currentRoute == AtlasScreen.Profile.name,
            onClick  = {
                navController.navigate(AtlasScreen.Profile.name) {
                    popUpTo(AtlasScreen.Home.name) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon     = { Icon(Icons.Outlined.PersonSearch, contentDescription = null) },
            label    = { Text("Profile") },
            colors   = NavigationBarItemDefaults.colors(
                selectedIconColor   = AtlasSky,
                selectedTextColor   = AtlasSky,
                indicatorColor      = AtlasCard,
                unselectedIconColor = AtlasText,
                unselectedTextColor = AtlasText
            )
        )
    }
}
