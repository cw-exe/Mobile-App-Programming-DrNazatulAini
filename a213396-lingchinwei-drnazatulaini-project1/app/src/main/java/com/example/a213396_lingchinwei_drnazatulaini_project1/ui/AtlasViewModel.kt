package com.example.a213396_lingchinwei_drnazatulaini_project1.ui

// This ViewModel holds ALL the data for the entire app.
// It is shared between all 5 screens so they all read from the same place.
// Data stored here: help requests the student submitted, volunteer list, profile info, courage points.

import androidx.lifecycle.ViewModel
import com.example.a213396_lingchinwei_drnazatulaini_project1.data.HelpRequest
import com.example.a213396_lingchinwei_drnazatulaini_project1.data.UserProfile
import com.example.a213396_lingchinwei_drnazatulaini_project1.data.Volunteer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AtlasViewModel : ViewModel() {

    // The list of help requests the student has submitted via the Ask screen
    private val _helpRequests = MutableStateFlow<List<HelpRequest>>(emptyList())
    val helpRequests: StateFlow<List<HelpRequest>> = _helpRequests.asStateFlow()

    // The pre-loaded list of volunteer mentors (no database needed — stored in memory)
    private val _volunteers = MutableStateFlow(buildVolunteerList())
    val volunteers: StateFlow<List<Volunteer>> = _volunteers.asStateFlow()

    // The student's name and ID entered on the Profile screen
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    // Courage Points go up by 1 each time the student asks for help
    private val _couragePoints = MutableStateFlow(0)
    val couragePoints: StateFlow<Int> = _couragePoints.asStateFlow()

    // Keeps track of which volunteer IDs the student has booked a session with
    private val _bookedVolunteerIds = MutableStateFlow<Set<Int>>(emptySet())
    val bookedVolunteerIds: StateFlow<Set<Int>> = _bookedVolunteerIds.asStateFlow()

    // Adds a new help request to the list and gives the student +1 Courage Point
    fun addHelpRequest(request: HelpRequest) {
        _helpRequests.update { currentList -> currentList + request }
        _couragePoints.update { currentPoints -> currentPoints + 1 }
    }

    // Updates the student's name (called when they type in the name field on Profile screen)
    fun updateStudentName(name: String) {
        _userProfile.update { it.copy(studentName = name) }
    }

    // Updates the student's ID (called when they type in the ID field on Profile screen)
    fun updateStudentId(id: String) {
        _userProfile.update { it.copy(studentId = id) }
    }

    // Marks a volunteer as booked by saving their ID in the booked set
    fun bookVolunteer(volunteerId: Int) {
        _bookedVolunteerIds.update { currentSet -> currentSet + volunteerId }
    }

    // Creates the starting list of 6 volunteer mentors from Tutors in Action Malaysia
    private fun buildVolunteerList(): List<Volunteer> {
        return listOf(
            Volunteer(
                id = 1,
                name = "Ahmad Faris",
                subjects = listOf("Math", "Add Maths"),
                availability = "Weekends, 9am–12pm",
                tags = listOf("Patient", "Bilingual"),
                story = "I failed Add Maths in Form 4 and retook it. Best decision I made."
            ),
            Volunteer(
                id = 2,
                name = "Priya Nair",
                subjects = listOf("English", "Bahasa Malaysia"),
                availability = "Weekdays, 7pm–9pm",
                tags = listOf("Encouraging", "Direct"),
                story = "English was my weakest subject. I cried over essays. Now I teach them."
            ),
            Volunteer(
                id = 3,
                name = "Wei Ling",
                subjects = listOf("Chemistry", "Biology"),
                availability = "Weekends, 2pm–5pm",
                tags = listOf("Energetic"),
                story = "I almost dropped Science stream. A volunteer changed my mind."
            ),
            Volunteer(
                id = 4,
                name = "Hafiz Roslan",
                subjects = listOf("Physics", "Math"),
                availability = "Weekends, 10am–1pm",
                tags = listOf("Patient", "Bilingual"),
                story = "I tutored myself using YouTube for 6 months. I know what it feels like."
            ),
            Volunteer(
                id = 5,
                name = "Siti Aisyah",
                subjects = listOf("History", "Bahasa Malaysia"),
                availability = "Weekdays, 8pm–10pm",
                tags = listOf("Encouraging"),
                story = "History felt pointless to me until someone showed me why it matters."
            ),
            Volunteer(
                id = 6,
                name = "Rajan Kumar",
                subjects = listOf("Biology", "Chemistry"),
                availability = "Weekends, 3pm–6pm",
                tags = listOf("Direct"),
                story = "Failed SPM Biology first try. Passed with A on second. Never give up."
            )
        )
    }
}
