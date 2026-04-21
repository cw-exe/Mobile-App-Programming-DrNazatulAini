package com.example.a213396_lingchinwei_drnazatulaini_lab4.ui

import androidx.lifecycle.ViewModel
import com.example.a213396_lingchinwei_drnazatulaini_lab4.data.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UserProfile())
    val uiState: StateFlow<UserProfile> = _uiState.asStateFlow()

    fun updateStudentName(name: String) = _uiState.update { it.copy(studentName = name) }
    fun updateStudentId(id: String) = _uiState.update { it.copy(studentId = id) }
}
