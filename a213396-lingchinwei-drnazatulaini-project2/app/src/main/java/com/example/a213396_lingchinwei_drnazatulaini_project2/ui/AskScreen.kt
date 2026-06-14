package com.example.a213396_lingchinwei_drnazatulaini_project2.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.a213396_lingchinwei_drnazatulaini_project2.AtlasScreen
import com.example.a213396_lingchinwei_drnazatulaini_project2.BottomNavigationBar
import com.example.a213396_lingchinwei_drnazatulaini_project2.data.HelpRequest
import com.example.a213396_lingchinwei_drnazatulaini_project2.ui.theme.AtlasBlue
import com.example.a213396_lingchinwei_drnazatulaini_project2.ui.theme.AtlasNavy
import com.example.a213396_lingchinwei_drnazatulaini_project2.ui.theme.AtlasSky
import com.example.a213396_lingchinwei_drnazatulaini_project2.ui.theme.AtlasText
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

@Composable
fun AskScreen(navController: NavHostController, viewModel: AtlasViewModel) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    var selectedSubject   by remember { mutableStateOf("") }
    var questionText      by remember { mutableStateOf("") }
    var isUrgent          by remember { mutableStateOf(false) }
    var contactPreference by remember { mutableStateOf("Chat") }
    var isAnonymous       by remember { mutableStateOf(true) }
    var shareToCommunity  by remember { mutableStateOf(false) }
    var helpType          by remember { mutableStateOf("Online") }
    var phoneNumber       by remember { mutableStateOf("") }
    var showPermissionDenied by remember { mutableStateOf(false) }
    var pendingRequest    by remember { mutableStateOf<HelpRequest?>(null) }

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val availableSubjects = listOf(
        "Math", "Science", "Bahasa Malaysia", "English",
        "History", "Chemistry", "Physics", "Biology", "Add Maths"
    )

    val isFormValid = selectedSubject.isNotBlank() && questionText.isNotBlank()

    // Navigates after submit so the student sees their request
    fun navigateToSessions() {
        navController.navigate(AtlasScreen.Sessions.name) {
            popUpTo(AtlasScreen.Home.name) { saveState = true }
            launchSingleTop = true
        }
    }

    // Gets location then calls submitHelpRequest — called when permission is already held
    val getLocationAndSubmit: (HelpRequest) -> Unit = { req ->
        val hasPermission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (hasPermission) {
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).addOnSuccessListener { location ->
                viewModel.submitHelpRequest(
                    request          = req,
                    lat              = location?.latitude,
                    lon              = location?.longitude,
                    shareToCommunity = true
                )
                navigateToSessions()
            }.addOnFailureListener {
                viewModel.submitHelpRequest(req, shareToCommunity = true)
                navigateToSessions()
            }
        } else {
            viewModel.submitHelpRequest(req, shareToCommunity = true)
            navigateToSessions()
        }
    }

    // Handles the permission dialog result
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                      permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val req = pendingRequest ?: return@rememberLauncherForActivityResult
        pendingRequest = null
        if (granted) {
            getLocationAndSubmit(req)
        } else {
            showPermissionDenied = true
            viewModel.submitHelpRequest(req, shareToCommunity = true)
            navigateToSessions()
        }
    }

    val scrollState = rememberScrollState()
    // When community toggle turns on, scroll to reveal the newly expanded chips.
    // The 150ms delay lets Compose finish laying out the new content before scrolling.
    LaunchedEffect(shareToCommunity) {
        if (shareToCommunity) {
            delay(150)
            scrollState.animateScrollTo(scrollState.maxValue)
        }
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
                        text     = "No question is too small — every request here reaches a real volunteer tutor.",
                        fontSize = 14.sp,
                        color    = AtlasText.copy(alpha = 0.7f)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SubjectPickerSection(
                    selectedSubject   = selectedSubject,
                    onSubjectSelected = { selectedSubject = it },
                    subjects          = availableSubjects
                )

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

                UrgencyPickerCard(isUrgent = isUrgent, onToggle = { isUrgent = it })

                ContactPreferenceCard(
                    selectedPreference = contactPreference,
                    onSelect           = { contactPreference = it }
                )

                AnonymousToggleCard(isAnonymous = isAnonymous, onToggle = {
                    isAnonymous = it
                    if (it) phoneNumber = ""
                })

                if (!isAnonymous) {
                    OutlinedTextField(
                        value         = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label         = { Text("Contact number (optional)") },
                        placeholder   = { Text("e.g. 012-3456789", color = AtlasNavy.copy(alpha = 0.4f)) },
                        leadingIcon   = { Icon(Icons.Default.Phone, contentDescription = null, tint = AtlasNavy.copy(alpha = 0.5f)) },
                        singleLine    = true,
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        colors        = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor   = AtlasBlue,
                            unfocusedBorderColor = AtlasNavy.copy(alpha = 0.2f)
                        )
                    )
                }

                ShareToCommunityToggleCard(
                    enabled  = shareToCommunity,
                    onToggle = {
                        shareToCommunity = it
                        if (!it) {
                            showPermissionDenied = false
                            helpType = "Online"
                        }
                    },
                    helpType         = helpType,
                    onHelpTypeSelect = { helpType = it }
                )

                if (showPermissionDenied) {
                    Text(
                        text     = "Location permission denied — request saved locally only.",
                        color    = Color(0xFFFF5722),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }

                Button(
                    onClick = {
                        val newRequest = HelpRequest(
                            id                = 0,
                            subject           = selectedSubject,
                            question          = questionText,
                            isUrgent          = isUrgent,
                            isAnonymous       = isAnonymous,
                            contactPreference = contactPreference,
                            helpType          = if (shareToCommunity) helpType else "Online",
                            phoneNumber       = if (isAnonymous) "" else phoneNumber
                        )
                        if (shareToCommunity && helpType == "In-person") {
                            val alreadyGranted =
                                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            if (alreadyGranted) {
                                getLocationAndSubmit(newRequest)
                            } else {
                                pendingRequest = newRequest
                                permissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            }
                        } else if (shareToCommunity) {
                            // Online + community: post to board without GPS
                            viewModel.submitHelpRequest(newRequest, shareToCommunity = true)
                            navigateToSessions()
                        } else {
                            viewModel.submitHelpRequest(newRequest)
                            navigateToSessions()
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
                FilterChip(
                    selected = !isUrgent,
                    onClick  = { onToggle(false) },
                    label    = { Text("Normal") },
                    colors   = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AtlasBlue,
                        selectedLabelColor     = Color.White
                    )
                )
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

@Composable
fun AnonymousToggleCard(isAnonymous: Boolean, onToggle: (Boolean) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(12.dp),
        colors   = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
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

@Composable
fun ShareToCommunityToggleCard(
    enabled: Boolean,
    onToggle: (Boolean) -> Unit,
    helpType: String = "Online",
    onHelpTypeSelect: (String) -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(12.dp),
        colors   = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text       = "Share to Community Board",
                        fontWeight = FontWeight.SemiBold,
                        color      = AtlasNavy
                    )
                    Text(
                        text     = "Volunteers nearby or online can see and respond to your request",
                        fontSize = 12.sp,
                        color    = AtlasNavy.copy(alpha = 0.6f)
                    )
                }
                Switch(
                    checked         = enabled,
                    onCheckedChange = onToggle,
                    colors          = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = AtlasBlue
                    )
                )
            }

            if (enabled) {
                Text(
                    text       = "Help format",
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 13.sp,
                    color      = AtlasNavy
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FilterChip(
                        selected = helpType == "Online",
                        onClick  = { onHelpTypeSelect("Online") },
                        label    = { Text("Online") },
                        colors   = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AtlasBlue,
                            selectedLabelColor     = Color.White
                        )
                    )
                    FilterChip(
                        selected    = helpType == "In-person",
                        onClick     = { onHelpTypeSelect("In-person") },
                        label       = { Text("In-person (near me)") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Place,
                                contentDescription = null,
                                modifier           = Modifier.size(16.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AtlasBlue,
                            selectedLabelColor     = Color.White
                        )
                    )
                }
                if (helpType == "In-person") {
                    Text(
                        text     = "Your approximate area will be shared — no exact address stored",
                        fontSize = 11.sp,
                        color    = AtlasNavy.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

// getSubjectColor is defined as a package-level function in SessionsScreen.kt
// and is shared across all screens in this package.
