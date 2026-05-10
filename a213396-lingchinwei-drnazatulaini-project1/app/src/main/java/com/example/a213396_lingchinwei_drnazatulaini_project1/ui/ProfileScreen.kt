package com.example.a213396_lingchinwei_drnazatulaini_project1.ui

// This is the Profile screen (My Legacy) — the student's personal page.
// It reads from the ViewModel: name, student ID, courage points, sessions, and booked mentors.
// It also shows a "Pay It Forward" section once the student has booked at least one session.

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AccentGreen
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AtlasBlue
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AtlasNavy
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AtlasSky
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AtlasText

@Composable
fun ProfileScreen(navController: NavHostController, viewModel: AtlasViewModel) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Read all the data this screen needs from the shared ViewModel
    val userProfile        by viewModel.userProfile.collectAsState()
    val couragePoints      by viewModel.couragePoints.collectAsState()
    val helpRequestList    by viewModel.helpRequests.collectAsState()
    val bookedVolunteerIds by viewModel.bookedVolunteerIds.collectAsState()

    val totalSessionsBooked = bookedVolunteerIds.size
    val chainDepth          = helpRequestList.size + 1

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, currentRoute) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF0F4FF))
        ) {
            // Dark navy header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AtlasNavy)
                    .padding(horizontal = 24.dp, vertical = 28.dp)
            ) {
                Text(
                    text       = "My Legacy",
                    fontSize   = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color      = Color.White
                )
            }

            // Scrollable content area
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Circle avatar at the top of the screen
                ProfileAvatar()

                // Editable name and student ID fields
                ProfileInputFields(
                    studentName  = userProfile.studentName,
                    studentId    = userProfile.studentId,
                    onNameChange = viewModel::updateStudentName,
                    onIdChange   = viewModel::updateStudentId
                )

                // Student card visual — only appears once both fields are filled
                if (userProfile.studentName.isNotBlank() && userProfile.studentId.isNotBlank()) {
                    StudentCard(
                        studentName = userProfile.studentName,
                        studentId   = userProfile.studentId
                    )
                }

                // Stats card showing journey progress
                JourneyStatsCard(
                    couragePoints      = couragePoints,
                    sessionsCompleted  = totalSessionsBooked,
                    chainDepth         = chainDepth
                )

                // Pay It Forward section — appears after the student has booked at least 1 session
                if (totalSessionsBooked >= 1) {
                    PayItForwardCard(navController = navController)
                }

                // Partnership info at the very bottom
                TutorsInActionCard()
            }
        }
    }
}

// Circle avatar showing a person icon
@Composable
fun ProfileAvatar() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(
            modifier         = Modifier
                .size(88.dp)
                .background(AtlasBlue.copy(alpha = 0.15f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint     = AtlasBlue,
                modifier = Modifier.size(52.dp)
            )
        }
    }
}

// Card with editable text fields for the student's name and ID
@Composable
fun ProfileInputFields(
    studentName: String,
    studentId: String,
    onNameChange: (String) -> Unit,
    onIdChange: (String) -> Unit
) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text          = "MY DETAILS",
                fontSize      = 11.sp,
                fontWeight    = FontWeight.Black,
                color         = AtlasNavy.copy(alpha = 0.4f),
                letterSpacing = 1.sp
            )

            OutlinedTextField(
                value         = studentName,
                onValueChange = onNameChange,
                label         = { Text("Student Name") },
                singleLine    = true,
                modifier      = Modifier.fillMaxWidth(),
                shape         = RoundedCornerShape(12.dp),
                colors        = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor   = AtlasBlue,
                    unfocusedBorderColor = AtlasNavy.copy(alpha = 0.2f)
                )
            )

            OutlinedTextField(
                value         = studentId,
                onValueChange = onIdChange,
                label         = { Text("Student ID") },
                singleLine    = true,
                modifier      = Modifier.fillMaxWidth(),
                shape         = RoundedCornerShape(12.dp),
                colors        = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor   = AtlasBlue,
                    unfocusedBorderColor = AtlasNavy.copy(alpha = 0.2f)
                )
            )
        }
    }
}

// Dark student card showing the student's name and ID (kept from original AtlasLearn)
@Composable
fun StudentCard(studentName: String, studentId: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = AtlasNavy)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text          = "STUDENT CARD",
                fontSize      = 11.sp,
                fontWeight    = FontWeight.Black,
                color         = AtlasSky,
                letterSpacing = 1.sp
            )
            HorizontalDivider(color = AtlasSky.copy(alpha = 0.3f))
            StudentCardRow(label = "Name", value = studentName)
            StudentCardRow(label = "ID",   value = studentId)
        }
    }
}

// A single label + value row inside the student card
@Composable
fun StudentCardRow(label: String, value: String) {
    Row(
        modifier             = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = AtlasSky.copy(alpha = 0.7f), fontSize = 13.sp)
        Text(text = value, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
    }
}

// Card showing courage points, sessions completed, and chain depth
@Composable
fun JourneyStatsCard(couragePoints: Int, sessionsCompleted: Int, chainDepth: Int) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text          = "MY JOURNEY",
                fontSize      = 11.sp,
                fontWeight    = FontWeight.Black,
                color         = AtlasNavy.copy(alpha = 0.4f),
                letterSpacing = 1.sp
            )

            // Courage Points earned from asking for help
            StatRow(
                icon      = Icons.Default.LocalFireDepartment,
                iconColor = AccentGold,
                label     = "Courage Points",
                value     = couragePoints.toString()
            )

            HorizontalDivider(color = AtlasNavy.copy(alpha = 0.08f))

            // How many mentor sessions have been booked
            StatRow(
                icon      = Icons.Default.People,
                iconColor = AtlasBlue,
                label     = "Sessions Completed",
                value     = sessionsCompleted.toString()
            )

            HorizontalDivider(color = AtlasNavy.copy(alpha = 0.08f))

            // Chain depth shows how many people are in the student's learning chain
            StatRow(
                icon      = Icons.Default.Link,
                iconColor = AccentGreen,
                label     = "Chain Depth",
                value     = "Part of a chain of $chainDepth learners"
            )
        }
    }
}

// A single row in the stats card: icon, label, and value
@Composable
fun StatRow(icon: ImageVector, iconColor: Color, label: String, value: String) {
    Row(
        modifier             = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment    = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment    = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(22.dp))
            Text(text = label, fontSize = 14.sp, color = AtlasNavy, fontWeight = FontWeight.Medium)
        }
        Text(text = value, fontSize = 14.sp, color = AtlasNavy, fontWeight = FontWeight.Bold)
    }
}

// Card encouraging the student to become a mentor — shows after booking 1+ session
@Composable
fun PayItForwardCard(navController: NavHostController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = AccentGreen.copy(alpha = 0.15f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment    = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.VolunteerActivism,
                    contentDescription = null,
                    tint     = AccentGreen,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text       = "Ready to Pay It Forward?",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color      = AtlasNavy
                )
            }
            Text(
                text     = "Ready to help someone younger? Join as a volunteer.",
                fontSize = 14.sp,
                color    = AtlasNavy.copy(alpha = 0.8f)
            )
            Button(
                onClick = { navController.navigate(AtlasScreen.Match.name) },
                shape   = RoundedCornerShape(12.dp),
                colors  = ButtonDefaults.buttonColors(containerColor = AccentGreen)
            ) {
                Text(
                    text       = "Join as a Volunteer",
                    color      = AtlasNavy,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Card at the bottom showing Tutors in Action partnership information
@Composable
fun TutorsInActionCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = AtlasNavy)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment    = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Handshake,
                    contentDescription = null,
                    tint     = AtlasSky,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text       = "Our Partner",
                    fontSize   = 13.sp,
                    color      = AtlasSky,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(
                text       = "AtlasLearn is proudly partnered with Tutors in Action Malaysia — providing free tutoring to B40 students since 2010.",
                fontSize   = 13.sp,
                color      = AtlasText.copy(alpha = 0.8f),
                lineHeight = 20.sp
            )
        }
    }
}
