package com.example.a213396_lingchinwei_drnazatulaini_project1.ui

// This is the Ask screen (Spark Room) — the student fills out this form to request help from a mentor.
// When they press submit, the request is saved to the ViewModel and they earn a Courage Point.
// After submitting, the app navigates to the Sessions screen so they can see their request appear.

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AtlasBlue
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AtlasNavy
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AtlasText

@Composable
fun AskScreen(navController: NavHostController, viewModel: AtlasViewModel) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // These are the form fields — each one is stored as local state on this screen
    var selectedSubject   by remember { mutableStateOf("") }
    var questionText      by remember { mutableStateOf("") }
    var isUrgent          by remember { mutableStateOf(false) }
    var contactPreference by remember { mutableStateOf("Chat") }
    var isAnonymous       by remember { mutableStateOf(true) }

    // Read how many requests exist so we can give the new one a unique ID
    val helpRequestList by viewModel.helpRequests.collectAsState()
    val nextRequestId = helpRequestList.size + 1

    // The list of subjects a student can ask for help with
    val availableSubjects = listOf(
        "Math", "Science", "Bahasa Malaysia", "English",
        "History", "Chemistry", "Physics", "Biology", "Add Maths"
    )

    // The form is only valid if a subject and a question are both filled in
    val isFormValid = selectedSubject.isNotBlank() && questionText.isNotBlank()

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
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text       = "Spark Room",
                        fontSize   = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color      = Color.White
                    )
                    Text(
                        text     = "Ask for help — it takes courage.",
                        fontSize = 14.sp,
                        color    = AtlasText.copy(alpha = 0.7f)
                    )
                }
            }

            // Scrollable form area
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Step 1: Pick a subject using chips
                SubjectPickerSection(
                    selectedSubject  = selectedSubject,
                    onSubjectSelected = { selectedSubject = it },
                    subjects         = availableSubjects
                )

                // Step 2: Type the question
                OutlinedTextField(
                    value         = questionText,
                    onValueChange = { questionText = it },
                    label         = { Text("What do you need help with?") },
                    placeholder   = { Text("Describe your question here...", color = AtlasNavy.copy(alpha = 0.4f)) },
                    minLines      = 4,
                    maxLines      = 6,
                    modifier      = Modifier.fillMaxWidth(),
                    shape         = RoundedCornerShape(12.dp),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = AtlasBlue,
                        unfocusedBorderColor = AtlasNavy.copy(alpha = 0.2f)
                    )
                )

                // Step 3: Choose Normal or SOS urgency
                UrgencyPickerCard(isUrgent = isUrgent, onToggle = { isUrgent = it })

                // Step 4: Choose Chat or Voice Call contact preference
                ContactPreferenceCard(
                    selectedPreference = contactPreference,
                    onSelect           = { contactPreference = it }
                )

                // Step 5: Toggle whether to stay anonymous (on by default)
                AnonymousToggleCard(isAnonymous = isAnonymous, onToggle = { isAnonymous = it })

                // Submit button — only enabled when the form is valid
                Button(
                    onClick = {
                        // Build the HelpRequest object from the form data
                        val newRequest = HelpRequest(
                            id                = nextRequestId,
                            subject           = selectedSubject,
                            question          = questionText,
                            isUrgent          = isUrgent,
                            isAnonymous       = isAnonymous,
                            contactPreference = contactPreference
                        )
                        // Save the request and earn a Courage Point
                        viewModel.addHelpRequest(newRequest)
                        // Go to Sessions screen so the student sees their request listed
                        navController.navigate(AtlasScreen.Sessions.name) {
                            popUpTo(AtlasScreen.Home.name) { saveState = true }
                            launchSingleTop = true
                        }
                    },
                    enabled  = isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape  = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AtlasBlue)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Send My Request", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// Scrollable row of subject chips — tapping one selects it
@Composable
fun SubjectPickerSection(
    selectedSubject: String,
    onSubjectSelected: (String) -> Unit,
    subjects: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(12.dp),
        colors   = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "Subject", fontWeight = FontWeight.SemiBold, color = AtlasNavy)

            // Each subject is a tappable chip — tapping one highlights it
            Row(
                modifier              = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                subjects.forEach { subject ->
                    FilterChip(
                        selected = selectedSubject == subject,
                        onClick  = { onSubjectSelected(subject) },
                        label    = { Text(subject, fontSize = 13.sp) },
                        colors   = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = getSubjectColor(subject).copy(alpha = 0.85f),
                            selectedLabelColor     = Color.White
                        )
                    )
                }
            }
        }
    }
}

// Card with Normal and SOS urgency chips
@Composable
fun UrgencyPickerCard(isUrgent: Boolean, onToggle: (Boolean) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(12.dp),
        colors   = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "Urgency", fontWeight = FontWeight.SemiBold, color = AtlasNavy)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Normal — not urgent
                FilterChip(
                    selected = !isUrgent,
                    onClick  = { onToggle(false) },
                    label    = { Text("Normal") },
                    colors   = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AtlasBlue,
                        selectedLabelColor     = Color.White
                    )
                )
                // SOS — exam is coming soon
                FilterChip(
                    selected    = isUrgent,
                    onClick     = { onToggle(true) },
                    label       = { Text("SOS — Exam Soon!") },
                    leadingIcon = {
                        if (isUrgent) Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFFF5722),
                        selectedLabelColor     = Color.White
                    )
                )
            }
        }
    }
}

// Card to choose Chat or Voice Call contact preference
@Composable
fun ContactPreferenceCard(selectedPreference: String, onSelect: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(12.dp),
        colors   = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text       = "How should your mentor contact you?",
                fontWeight = FontWeight.SemiBold,
                color      = AtlasNavy
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FilterChip(
                    selected    = selectedPreference == "Chat",
                    onClick     = { onSelect("Chat") },
                    label       = { Text("Chat") },
                    leadingIcon = {
                        Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AtlasBlue,
                        selectedLabelColor     = Color.White
                    )
                )
                FilterChip(
                    selected    = selectedPreference == "Voice Call",
                    onClick     = { onSelect("Voice Call") },
                    label       = { Text("Voice Call") },
                    leadingIcon = {
                        Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AtlasBlue,
                        selectedLabelColor     = Color.White
                    )
                )
            }
        }
    }
}

// Card with a toggle to keep the request anonymous
@Composable
fun AnonymousToggleCard(isAnonymous: Boolean, onToggle: (Boolean) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(12.dp),
        colors   = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier             = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment    = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text       = "Stay Anonymous",
                    fontWeight = FontWeight.SemiBold,
                    color      = AtlasNavy
                )
                Text(
                    text     = "Your name will not be shown to the mentor",
                    fontSize = 12.sp,
                    color    = AtlasNavy.copy(alpha = 0.6f)
                )
            }
            Switch(
                checked         = isAnonymous,
                onCheckedChange = onToggle,
                colors          = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = AtlasBlue
                )
            )
        }
    }
}
