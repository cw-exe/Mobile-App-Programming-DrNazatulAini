package com.example.a213396_lingchinwei_drnazatulaini_lab4.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.a213396_lingchinwei_drnazatulaini_lab4.BottomNavigationBar
import com.example.a213396_lingchinwei_drnazatulaini_lab4.data.UserProfile
import com.example.a213396_lingchinwei_drnazatulaini_lab4.ui.theme.*

@Composable
fun ProfileScreen(
    navController: NavHostController,
    uiState: UserProfile,
    onNameChange: (String) -> Unit,
    onIdChange: (String) -> Unit
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

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
                    .height(120.dp)
                    .background(AtlasNavy)
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text("Profile", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Avatar
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .background(AtlasBlue.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = AtlasBlue, modifier = Modifier.size(52.dp))
                    }
                }

                // Input fields
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "MY DETAILS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = AtlasNavy.copy(alpha = 0.4f),
                            letterSpacing = 1.sp
                        )
                        OutlinedTextField(
                            value = uiState.studentName,
                            onValueChange = onNameChange,
                            label = { Text("Student Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor   = AtlasBlue,
                                unfocusedBorderColor = AtlasNavy.copy(alpha = 0.2f)
                            )
                        )
                        OutlinedTextField(
                            value = uiState.studentId,
                            onValueChange = onIdChange,
                            label = { Text("Student ID") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor   = AtlasBlue,
                                unfocusedBorderColor = AtlasNavy.copy(alpha = 0.2f)
                            )
                        )
                    }
                }

                // Student card — appears once both fields are filled
                if (uiState.studentName.isNotBlank() && uiState.studentId.isNotBlank()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = AtlasNavy)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("STUDENT CARD", fontSize = 11.sp, fontWeight = FontWeight.Black, color = AtlasSky, letterSpacing = 1.sp)
                            HorizontalDivider(color = AtlasSky.copy(alpha = 0.3f))
                            ProfileDetailRow("Name", uiState.studentName)
                            ProfileDetailRow("ID",   uiState.studentId)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileDetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = AtlasSky.copy(alpha = 0.7f), fontSize = 13.sp)
        Text(value, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
    }
}
