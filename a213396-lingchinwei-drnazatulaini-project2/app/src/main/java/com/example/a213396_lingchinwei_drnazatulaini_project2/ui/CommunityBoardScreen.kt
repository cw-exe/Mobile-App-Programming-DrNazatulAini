package com.example.a213396_lingchinwei_drnazatulaini_project2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.IconButton
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

@Composable
fun CommunityBoardScreen(navController: NavHostController, viewModel: AtlasViewModel) {
    val communityRequests by viewModel.communityRequests.collectAsState()

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
                        text = "Community Board",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "SDG 4 — Real requests from students near you. Step up.",
                        fontSize = 12.sp,
                        color = AtlasSky
                    )
                }
            }
        }

        if (communityRequests.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.Groups,
                    contentDescription = null,
                    tint = AtlasNavy.copy(alpha = 0.3f),
                    modifier = Modifier.size(72.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No open requests yet.",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AtlasNavy
                )
                Text(
                    text = "Be the first to share your question publicly.",
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
                items(communityRequests, key = { it.id }) { request ->
                    CommunityRequestCard(
                        request = request,
                        onClick = { navController.navigate("RequestDetail/${request.id}") }
                    )
                }
            }
        }
    }
}

@Composable
fun CommunityRequestCard(request: CommunityHelpRequest, onClick: () -> Unit) {
    // Subtle subject-color tint — same palette as the chip — ties the card to its topic visually
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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

            // Question preview — truncated to 2 lines
            Text(
                text = request.question,
                fontSize = 14.sp,
                color = AtlasNavy,
                maxLines = 2,
                lineHeight = 20.sp
            )

            // Show area badge for in-person requests, online badge otherwise
            if (request.helpType == "In-person" && request.areaName.isNotBlank()) {
                NominatimAreaBadge(areaName = request.areaName)
            } else if (request.helpType != "In-person") {
                OnlineHelpBadge()
            }

            // Requester name footer
            if (request.requesterName.isNotBlank()) {
                Text(
                    text = "by ${request.requesterName}",
                    fontSize = 12.sp,
                    color = AtlasNavy.copy(alpha = 0.45f)
                )
            }
        }
    }
}

// Badge shown on community cards where the student requested online help.
@Composable
fun OnlineHelpBadge(modifier: Modifier = Modifier) {
    Surface(
        shape    = RoundedCornerShape(20.dp),
        color    = AtlasBlue.copy(alpha = 0.10f),
        modifier = modifier
    ) {
        Row(
            modifier              = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                Icons.Default.Language,
                contentDescription = "Online tutoring",
                tint     = AtlasBlue,
                modifier = Modifier.size(13.dp)
            )
            Text(
                text       = "Online tutoring",
                fontSize   = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color      = AtlasBlue
            )
        }
    }
}

// Shared badge that makes the Nominatim API result visually unmistakable.
// Used in CommunityRequestCard and RequestDetailScreen so both screens show the same treatment.
@Composable
fun NominatimAreaBadge(areaName: String, modifier: Modifier = Modifier) {
    val teal = Color(0xFF00897B)
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = teal.copy(alpha = 0.10f),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                Icons.Default.Public,
                contentDescription = "Location resolved via Nominatim API",
                tint = teal,
                modifier = Modifier.size(13.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Text(
                    text = "Nominatim API",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = teal,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = areaName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = teal
                )
            }
        }
    }
}
