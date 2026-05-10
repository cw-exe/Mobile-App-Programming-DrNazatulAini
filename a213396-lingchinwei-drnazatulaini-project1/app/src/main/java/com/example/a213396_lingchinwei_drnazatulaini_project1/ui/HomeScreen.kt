package com.example.a213396_lingchinwei_drnazatulaini_project1.ui

// This is the Home screen — the first thing students see when they open AtlasLearn.
// It shows the mentorship chain concept, community stats, two main action buttons,
// and inspiring breakthrough stories from other students.

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.a213396_lingchinwei_drnazatulaini_project1.AtlasScreen
import com.example.a213396_lingchinwei_drnazatulaini_project1.BottomNavigationBar
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AccentGold
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AtlasBlue
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AtlasNavy
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AtlasSky
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AtlasText

@Composable
fun HomeScreen(navController: NavHostController, viewModel: AtlasViewModel) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Read the student's name from the ViewModel so we can greet them by name
    val userProfile by viewModel.userProfile.collectAsState()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, currentRoute) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF0F4FF))
                .verticalScroll(rememberScrollState())
        ) {
            // Dark navy header at the top
            HomeHeader(
                studentName  = userProfile.studentName,
                navController = navController
            )

            // All the cards below the header
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Task 2 — Cards: greeting message wrapped inside a Card composable.
                // Shows a personalised welcome when a name is set, or an onboarding prompt when not.
                PersonalisedGreetingCard(studentName = userProfile.studentName)

                // Shows how many students were helped this week in Malaysia
                CommunityStatsCard()

                // Two big buttons: Ask for Help and Volunteer
                ActionButtonsRow(navController = navController)

                // Three inspiring stories from the community
                RecentBreakthroughsSection()

                // Tutors in Action partnership info at the bottom
                PartnershipCard()
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// The dark navy header showing the app name and partnership badge
@Composable
fun HomeHeader(studentName: String, navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(AtlasNavy)
            .padding(horizontal = 24.dp, vertical = 28.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text       = "AtlasLearn",
                fontSize   = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = Color.White
            )

            // Small badge showing the Tutors in Action partnership
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = AtlasBlue.copy(alpha = 0.7f)
            ) {
                Row(
                    modifier             = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                    verticalAlignment    = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        Icons.Default.Verified,
                        contentDescription = null,
                        tint     = AtlasSky,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text       = "Tutors in Action Malaysia",
                        fontSize   = 12.sp,
                        color      = AtlasSky,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Text(
                text       = "The Chain of Mentorship",
                fontSize   = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color      = Color.White.copy(alpha = 0.9f)
            )
            Text(
                text     = "Every student helped today becomes a mentor tomorrow.",
                fontSize = 13.sp,
                color    = Color.White.copy(alpha = 0.65f)
            )

            // Personalised greeting once the student has entered their name
            if (studentName.isNotBlank()) {
                Text(
                    text       = "Welcome back, $studentName",
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = AtlasSky
                )
            }
        }
    }
}

// Card that shows how many students were helped this week
@Composable
fun CommunityStatsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = AtlasNavy)
    ) {
        Row(
            modifier             = Modifier.padding(20.dp),
            verticalAlignment    = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                Icons.Default.Groups,
                contentDescription = null,
                tint     = AtlasSky,
                modifier = Modifier.size(36.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text       = "247 students helped in Malaysia",
                    fontSize   = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color.White
                )
                Text(
                    text     = "this week alone — and it's 100% free.",
                    fontSize = 13.sp,
                    color    = AtlasText.copy(alpha = 0.7f)
                )
                Text(
                    text     = "SDG 4: Quality Education for every Malaysian.",
                    fontSize = 11.sp,
                    color    = AtlasSky.copy(alpha = 0.8f)
                )
            }
        }
    }
}

// Two big call-to-action buttons side by side
@Composable
fun ActionButtonsRow(navController: NavHostController) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Primary button: student needs help
        Button(
            onClick  = { navController.navigate(AtlasScreen.Ask.name) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape  = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AtlasBlue)
        ) {
            Icon(
                Icons.Default.Help,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "I Need Help", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        // Secondary button: student wants to volunteer
        OutlinedButton(
            onClick  = { navController.navigate(AtlasScreen.Match.name) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape  = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(2.dp, AtlasNavy),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = AtlasNavy)
        ) {
            Icon(
                Icons.Default.VolunteerActivism,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "I Want to Volunteer", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// Section showing three hardcoded breakthrough stories from the community
@Composable
fun RecentBreakthroughsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text       = "Recent Breakthroughs",
            fontSize   = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color      = AtlasNavy
        )

        // Each story is shown as a small card
        BreakthroughCard(
            icon  = Icons.Default.AutoAwesome,
            story = "A student in Selangor just understood Quadratic Equations for the first time."
        )
        BreakthroughCard(
            icon  = Icons.Default.EmojiEvents,
            story = "A Form 4 student in Johor finally cracked SPM Chemistry after 3 sessions."
        )
        BreakthroughCard(
            icon  = Icons.Default.Favorite,
            story = "A student in Kelantan scored their first A in English after working with a mentor."
        )
    }
}

// A single card showing one breakthrough story
@Composable
fun BreakthroughCard(icon: ImageVector, story: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(12.dp),
        colors   = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier             = Modifier.padding(16.dp),
            verticalAlignment    = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint     = AccentGold,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text       = story,
                fontSize   = 14.sp,
                color      = AtlasNavy,
                lineHeight = 20.sp
            )
        }
    }
}

// Card at the bottom showing the Tutors in Action partnership
@Composable
fun PartnershipCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = AtlasBlue.copy(alpha = 0.1f))
    ) {
        Row(
            modifier             = Modifier.padding(16.dp),
            verticalAlignment    = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                Icons.Default.Handshake,
                contentDescription = null,
                tint     = AtlasBlue,
                modifier = Modifier.size(28.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text     = "Proudly partnered with",
                    fontSize = 12.sp,
                    color    = AtlasNavy.copy(alpha = 0.6f)
                )
                Text(
                    text       = "Tutors in Action Malaysia",
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color      = AtlasNavy
                )
                Text(
                    text     = "Free tutoring for B40 students since 2010.",
                    fontSize = 12.sp,
                    color    = AtlasNavy.copy(alpha = 0.7f)
                )
            }
        }
    }
}

// Task 2 — Cards: wraps the greeting message inside a Card composable.
// If the student has entered their name, it shows "Welcome back, [Name]!".
// If no name yet, it shows a friendly prompt to go set up their profile.
@Composable
fun PersonalisedGreetingCard(studentName: String) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = AtlasBlue),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier             = Modifier.padding(20.dp),
            verticalAlignment    = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Circle avatar icon
            Box(
                modifier         = Modifier
                    .size(48.dp)
                    .background(Color.White.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint     = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Greeting text changes depending on whether the name is set
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                if (studentName.isNotBlank()) {
                    Text(
                        text       = "Welcome back, $studentName!",
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color.White
                    )
                    Text(
                        text     = "Ready to learn something new today?",
                        fontSize = 13.sp,
                        color    = Color.White.copy(alpha = 0.8f)
                    )
                } else {
                    Text(
                        text       = "Hello, learner!",
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color.White
                    )
                    Text(
                        text     = "Go to Profile to set your name and get a personalised greeting.",
                        fontSize = 13.sp,
                        color    = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}
