package com.example.a213396_lingchinwei_drnazatulaini_lab5.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a213396_lingchinwei_drnazatulaini_lab5.data.HelpRequest
import com.example.a213396_lingchinwei_drnazatulaini_lab5.data.HelpRequestsRepository
import com.example.a213396_lingchinwei_drnazatulaini_lab5.data.UserProfile
import com.example.a213396_lingchinwei_drnazatulaini_lab5.data.Volunteer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AtlasViewModel(private val helpRequestsRepository: HelpRequestsRepository) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    // Help requests streamed live from Room — auto-updates UI when DB changes
    val helpRequests: StateFlow<List<HelpRequest>> =
        helpRequestsRepository.getAllHelpRequestsStream()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = emptyList()
            )

    // Courage Points derived from the count of stored help requests
    val couragePoints: StateFlow<Int> =
        helpRequestsRepository.getAllHelpRequestsStream()
            .map { it.size }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = 0
            )

    // Hardcoded volunteer list — no persistence needed
    private val _volunteers = MutableStateFlow(buildVolunteerList())
    val volunteers: StateFlow<List<Volunteer>> = _volunteers.asStateFlow()

    // Student profile — held in memory (persists across screens within a session)
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    // Tracks which volunteer IDs the student has booked
    private val _bookedVolunteerIds = MutableStateFlow<Set<Int>>(emptySet())
    val bookedVolunteerIds: StateFlow<Set<Int>> = _bookedVolunteerIds.asStateFlow()

    fun addHelpRequest(request: HelpRequest) {
        viewModelScope.launch {
            helpRequestsRepository.insertHelpRequest(request)
        }
    }

    fun deleteHelpRequest(request: HelpRequest) {
        viewModelScope.launch {
            helpRequestsRepository.deleteHelpRequest(request)
        }
    }

    fun updateStudentName(name: String) {
        _userProfile.update { it.copy(studentName = name) }
    }

    fun updateStudentId(id: String) {
        _userProfile.update { it.copy(studentId = id) }
    }

    fun bookVolunteer(volunteerId: Int) {
        _bookedVolunteerIds.update { currentSet -> currentSet + volunteerId }
    }

    private fun buildVolunteerList(): List<Volunteer> {
        return listOf(
            Volunteer(
                id = 1,
                name = "Fahim",
                subjects = listOf("Math", "Add Maths"),
                availability = "Weekends, 9am–12pm",
                tags = listOf("Patient", "Bilingual"),
                story = "I failed Add Maths in Form 4 and retook it. Best decision I made."
            ),
            Volunteer(
                id = 2,
                name = "Yasmin",
                subjects = listOf("English", "Bahasa Malaysia"),
                availability = "Weekdays, 7pm–9pm",
                tags = listOf("Encouraging", "Direct"),
                story = "English was my weakest subject. I cried over essays. Now I teach them."
            ),
            Volunteer(
                id = 3,
                name = "Chin Wei",
                subjects = listOf("Chemistry", "Biology"),
                availability = "Weekends, 2pm–5pm",
                tags = listOf("Energetic"),
                story = "I almost dropped Science stream. A volunteer changed my mind."
            ),
            Volunteer(
                id = 4,
                name = "Arep",
                subjects = listOf("Physics", "Math"),
                availability = "Weekends, 10am–1pm",
                tags = listOf("Patient", "Bilingual"),
                story = "I tutored myself using YouTube for 6 months. I know what it feels like."
            ),
            Volunteer(
                id = 5,
                name = "Diana Amira",
                subjects = listOf("History", "Bahasa Malaysia"),
                availability = "Weekdays, 8pm–10pm",
                tags = listOf("Encouraging"),
                story = "History felt pointless to me until someone showed me why it matters."
            ),
            Volunteer(
                id = 6,
                name = "Syamil Zaidi",
                subjects = listOf("Biology", "Chemistry"),
                availability = "Weekends, 3pm–6pm",
                tags = listOf("Direct"),
                story = "Failed SPM Biology first try. Passed with A on second. Never give up."
            )
        )
    }
}
