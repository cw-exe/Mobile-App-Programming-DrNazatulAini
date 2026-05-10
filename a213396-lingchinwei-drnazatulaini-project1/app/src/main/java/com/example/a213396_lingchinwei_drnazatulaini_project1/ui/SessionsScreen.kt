package com.example.a213396_lingchinwei_drnazatulaini_project1.ui

// This is the Sessions screen (My Requests) — it shows all help requests the student has submitted.
// The list is read from the ViewModel, so it automatically updates when the student submits a new request.
// This screen proves that the Ask screen and the Sessions screen share the same data (the ViewModel).

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.a213396_lingchinwei_drnazatulaini_project1.AtlasScreen
import com.example.a213396_lingchinwei_drnazatulaini_project1.BottomNavigationBar
import com.example.a213396_lingchinwei_drnazatulaini_project1.data.HelpRequest
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AccentGold
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AccentGreen
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AccentPurple
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AtlasBlue
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AtlasNavy

@Composable
fun SessionsScreen(navController: NavHostController, viewModel: AtlasViewModel) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Read the list of help requests and courage points from the ViewModel
    val helpRequestList by viewModel.helpRequests.collectAsState()
    val couragePoints   by viewModel.couragePoints.collectAsState()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, currentRoute) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF0F4FF))
        ) {
            // Dark navy header showing total Courage Points earned
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AtlasNavy)
                    .padding(horizontal = 24.dp, vertical = 28.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text       = "My Requests",
                        fontSize   = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color      = Color.White
                    )
                    // Show how many Courage Points the student has earned
                    Row(
                        verticalAlignment    = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint     = AccentGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text       = "$couragePoints Courage Point${if (couragePoints != 1) "s" else ""} earned",
                            fontSize   = 14.sp,
                            color      = AccentGold,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Show either the list of requests, or a message if the list is empty
            if (helpRequestList.isEmpty()) {
                EmptyRequestsMessage(navController = navController)
            } else {
                LazyColumn(
                    modifier        = Modifier.fillMaxSize(),
                    contentPadding  = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(helpRequestList) { request ->
                        HelpRequestCard(request = request)
                    }
                }
            }
        }
    }
}

// Message shown when the student has not submitted any requests yet
@Composable
fun EmptyRequestsMessage(navController: NavHostController) {
    Column(
        modifier             = Modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment  = Alignment.CenterHorizontally,
        verticalArrangement  = Arrangement.Center
    ) {
        Icon(
            Icons.Default.QuestionAnswer,
            contentDescription = null,
            tint     = AtlasNavy.copy(alpha = 0.3f),
            modifier = Modifier.size(72.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text       = "No requests yet.",
            fontSize   = 20.sp,
            fontWeight = FontWeight.Bold,
            color      = AtlasNavy
        )
        Text(
            text     = "Ask for help — it takes courage.",
            fontSize = 14.sp,
            color    = AtlasNavy.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { navController.navigate(AtlasScreen.Ask.name) },
            shape   = RoundedCornerShape(12.dp),
            colors  = ButtonDefaults.buttonColors(containerColor = AtlasBlue)
        ) {
            Text(text = "Ask for Help Now", fontWeight = FontWeight.Bold)
        }
    }
}

// A card showing the details of one help request
@Composable
fun HelpRequestCard(request: HelpRequest) {
    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Top row: subject chip on left, SOS and status chips on right
            Row(
                modifier             = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment    = Alignment.CenterVertically
            ) {
                // Color-coded chip showing the subject
                SubjectColorChip(subjectName = request.subject)

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Red SOS badge if the request is urgent
                    if (request.isUrgent) {
                        SosUrgencyBadge()
                    }
                    // Status chip showing Pending, Matched, or Completed
                    RequestStatusChip(status = request.status)
                }
            }

            // The student's question (shows up to 2 lines)
            Text(
                text      = request.question,
                fontSize  = 14.sp,
                color     = AtlasNavy,
                maxLines  = 2,
                lineHeight = 20.sp
            )

            // Bottom row: contact method and Courage Point earned
            Row(
                modifier             = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment    = Alignment.CenterVertically
            ) {
                // Show the contact preference icon and label
                Row(
                    verticalAlignment    = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector        = if (request.contactPreference == "Chat") Icons.Default.Chat else Icons.Default.Phone,
                        contentDescription = null,
                        tint     = AtlasNavy.copy(alpha = 0.5f),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text     = request.contactPreference,
                        fontSize = 12.sp,
                        color    = AtlasNavy.copy(alpha = 0.5f)
                    )
                }

                // Courage Point earned for this request
                Row(
                    verticalAlignment    = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint     = AccentGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text       = "+1 Courage Point",
                        fontSize   = 12.sp,
                        color      = AccentGold,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// Color-coded chip that shows the subject name
@Composable
fun SubjectColorChip(subjectName: String) {
    val chipColor = getSubjectColor(subjectName)
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = chipColor.copy(alpha = 0.2f)
    ) {
        Text(
            text     = subjectName,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color    = chipColor
        )
    }
}

// Red SOS badge shown when the request is urgent
@Composable
fun SosUrgencyBadge() {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFFF5722).copy(alpha = 0.15f)
    ) {
        Row(
            modifier             = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment    = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Icon(
                Icons.Default.Warning,
                contentDescription = null,
                tint     = Color(0xFFFF5722),
                modifier = Modifier.size(12.dp)
            )
            Text(
                text       = "SOS",
                fontSize   = 11.sp,
                fontWeight = FontWeight.Black,
                color      = Color(0xFFFF5722)
            )
        }
    }
}

// Chip that shows whether the request is Pending, Matched, or Completed
@Composable
fun RequestStatusChip(status: String) {
    val statusColor = when (status) {
        "Matched"   -> Color(0xFF4CAF50)
        "Completed" -> AtlasBlue
        else        -> AtlasNavy.copy(alpha = 0.4f)
    }
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = statusColor.copy(alpha = 0.15f)
    ) {
        Text(
            text     = status,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color    = statusColor
        )
    }
}

// Returns the right color for each subject: gold for Math, purple for Languages, green for Science
fun getSubjectColor(subjectName: String): Color {
    return when (subjectName) {
        "Math", "Add Maths"            -> AccentGold
        "English", "Bahasa Malaysia"   -> AccentPurple
        "Chemistry", "Biology",
        "Physics", "Science"           -> AccentGreen
        "History"                      -> Color(0xFFFFAB40)
        else                           -> AccentGold
    }
}
