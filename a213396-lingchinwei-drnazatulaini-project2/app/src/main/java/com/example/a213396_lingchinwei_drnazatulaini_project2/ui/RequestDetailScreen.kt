package com.example.a213396_lingchinwei_drnazatulaini_project2.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.a213396_lingchinwei_drnazatulaini_project2.ui.theme.AtlasBlue
import com.example.a213396_lingchinwei_drnazatulaini_project2.ui.theme.AtlasNavy
import com.example.a213396_lingchinwei_drnazatulaini_project2.ui.theme.AtlasSky

@Composable
fun RequestDetailScreen(
    requestId: String,
    navController: NavHostController,
    viewModel: AtlasViewModel
) {
    val communityRequests by viewModel.communityRequests.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val request = communityRequests.find { it.id == requestId }
    val context = LocalContext.current

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
                        text = "Request Detail",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "SDG 4 — Help a fellow learner access quality education",
                        fontSize = 12.sp,
                        color = AtlasSky
                    )
                }
            }
        }

        if (request == null) {
            // Still loading from Firestore or invalid ID
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AtlasBlue)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Subject, urgency and status chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SubjectColorChip(subjectName = request.subject)
                    if (request.isUrgent) SosUrgencyBadge()
                    RequestStatusChip(status = request.status)
                }

                // Full question
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = getSubjectColor(request.subject).copy(alpha = 0.15f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Question",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AtlasNavy.copy(alpha = 0.5f)
                        )
                        Text(
                            text = request.question,
                            fontSize = 15.sp,
                            color = AtlasNavy,
                            lineHeight = 22.sp
                        )
                    }
                }

                // Info card: area, student, responded by
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = getSubjectColor(request.subject).copy(alpha = 0.15f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Always show whether this is online or in-person tutoring
                        RequestInfoRow(
                            label = "Format",
                            value = if (request.helpType == "In-person") "In-person tutoring" else "Online tutoring"
                        )
                        // Area badge only for in-person requests with a resolved area
                        if (request.helpType == "In-person" && request.areaName.isNotBlank()) {
                            HorizontalDivider(color = AtlasNavy.copy(alpha = 0.06f))
                            NominatimAreaBadge(areaName = request.areaName)
                        }
                        if (request.requesterName.isNotBlank()) {
                            HorizontalDivider(color = AtlasNavy.copy(alpha = 0.06f))
                            RequestInfoRow(label = "Student", value = request.requesterName)
                        }
                        HorizontalDivider(color = AtlasNavy.copy(alpha = 0.06f))
                        RequestInfoRow(
                            label = "Contact preference",
                            value = request.contactPreference.ifBlank { "Chat" }
                        )
                        if (request.phoneNumber.isNotBlank()) {
                            HorizontalDivider(color = AtlasNavy.copy(alpha = 0.06f))
                            RequestInfoRow(label = "Phone", value = request.phoneNumber)
                        }
                        if (request.respondedBy.isNotBlank()) {
                            HorizontalDivider(color = AtlasNavy.copy(alpha = 0.06f))
                            RequestInfoRow(label = "Responded by", value = request.respondedBy)
                        }
                    }
                }

                // "I Can Help" button — only when request is still Open
                if (request.status == "Open") {
                    Button(
                        onClick = {
                            if (userProfile.studentName.isBlank()) {
                                // Guard: writing "Volunteer" to respondedBy would never appear in
                                // My Responses because myResponses only queries when name is non-blank.
                                Toast.makeText(
                                    context,
                                    "Set your name in Profile first to track your responses.",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                viewModel.respondToRequest(requestId, userProfile.studentName)
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AtlasBlue)
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "I Can Help",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    Text(
                        text = "You'll be marked as the responding volunteer for this request.",
                        fontSize = 12.sp,
                        color = AtlasNavy.copy(alpha = 0.5f),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun RequestInfoRow(
    label: String,
    value: String,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        leadingIcon?.invoke()
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = AtlasNavy.copy(alpha = 0.5f)
            )
            Text(
                text = value,
                fontSize = 14.sp,
                color = AtlasNavy
            )
        }
    }
}
