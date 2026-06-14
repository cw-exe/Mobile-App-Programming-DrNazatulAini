package com.example.a213396_lingchinwei_drnazatulaini_project2.ui

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.a213396_lingchinwei_drnazatulaini_project2.data.CommunityHelpRequest
import com.example.a213396_lingchinwei_drnazatulaini_project2.ui.theme.AtlasBlue
import com.example.a213396_lingchinwei_drnazatulaini_project2.ui.theme.AtlasNavy
import com.example.a213396_lingchinwei_drnazatulaini_project2.ui.theme.AtlasSky

private val ResolvedGreen = Color(0xFF4CAF50)

@Composable
fun MyResponsesScreen(navController: NavHostController, viewModel: AtlasViewModel) {
    val myResponses by viewModel.myResponses.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4FF))
    ) {
        // Header with back button and SDG 4 framing
        // statusBarsPadding() pushes content below the status bar while letting AtlasNavy
        // fill the full area behind it (same pattern as Scaffold-based screens).
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AtlasNavy)
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "My Responses",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "SDG 4 — Every response creates access to education",
                        fontSize = 12.sp,
                        color = AtlasSky
                    )
                }
            }
        }

        if (myResponses.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.VolunteerActivism,
                    contentDescription = null,
                    tint = AtlasNavy.copy(alpha = 0.3f),
                    modifier = Modifier.size(72.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No responses yet.",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AtlasNavy
                )
                Text(
                    text = "Visit the Community Board to help a student.",
                    fontSize = 14.sp,
                    color = AtlasNavy.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(myResponses, key = { it.id }) { request ->
                    MyResponseCard(
                        request = request,
                        onMarkResolved = { viewModel.resolveRequest(request.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun MyResponseCard(request: CommunityHelpRequest, onMarkResolved: () -> Unit) {
    // Subject-color tint matches the chip above — visually links card and category
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = getSubjectColor(request.subject).copy(alpha = 0.15f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Subject chip + urgency badge + status chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SubjectColorChip(subjectName = request.subject)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (request.isUrgent) SosUrgencyBadge()
                    RequestStatusChip(status = request.status)
                }
            }

            // Question preview
            Text(
                text = request.question,
                fontSize = 14.sp,
                color = AtlasNavy,
                maxLines = 2,
                lineHeight = 20.sp
            )

            // Area and requester footer
            if (request.areaName.isNotBlank() || request.requesterName.isNotBlank()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = request.areaName,
                        fontSize = 12.sp,
                        color = AtlasBlue.copy(alpha = 0.8f)
                    )
                    if (request.requesterName.isNotBlank()) {
                        Text(
                            text = "by ${request.requesterName}",
                            fontSize = 12.sp,
                            color = AtlasNavy.copy(alpha = 0.45f)
                        )
                    }
                }
            }

            // Action row: "Mark Resolved" button or resolved badge
            if (request.status == "Resolved") {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = ResolvedGreen.copy(alpha = 0.12f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = ResolvedGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Resolved",
                            fontWeight = FontWeight.Bold,
                            color = ResolvedGreen
                        )
                    }
                }
            } else {
                Button(
                    onClick = onMarkResolved,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ResolvedGreen)
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Mark Resolved", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
