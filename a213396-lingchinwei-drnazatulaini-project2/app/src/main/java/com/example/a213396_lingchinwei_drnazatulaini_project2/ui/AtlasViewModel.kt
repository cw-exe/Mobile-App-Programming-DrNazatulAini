package com.example.a213396_lingchinwei_drnazatulaini_project2.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a213396_lingchinwei_drnazatulaini_project2.data.CommunityHelpRepository
import com.example.a213396_lingchinwei_drnazatulaini_project2.data.CommunityHelpRequest
import com.example.a213396_lingchinwei_drnazatulaini_project2.data.HelpRequest
import com.example.a213396_lingchinwei_drnazatulaini_project2.data.HelpRequestsRepository
import com.example.a213396_lingchinwei_drnazatulaini_project2.data.LocationRepository
import com.example.a213396_lingchinwei_drnazatulaini_project2.data.UserProfile
import com.example.a213396_lingchinwei_drnazatulaini_project2.data.Volunteer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// AtlasViewModel is the single source of truth shared by every screen.
// It receives two repositories via constructor injection so AppViewModelProvider
// can supply them — the standard pattern from the Inventory App lab.
@OptIn(ExperimentalCoroutinesApi::class)
class AtlasViewModel(
    private val helpRequestsRepository: HelpRequestsRepository,
    private val communityHelpRepository: CommunityHelpRepository
) : ViewModel() {

    private val locationRepository = LocationRepository()

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    // ── Room streams ─────────────────────────────────────────────────────────

    // Live stream of the student's own help requests, read directly from Room.
    // stateIn converts the cold Room Flow into a hot StateFlow so every collector
    // shares the same upstream subscription.
    val helpRequests: StateFlow<List<HelpRequest>> =
        helpRequestsRepository.getAllHelpRequestsStream()
            .stateIn(
                scope   = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = emptyList()
            )

    // Courage Points = number of requests the student has ever submitted.
    val couragePoints: StateFlow<Int> =
        helpRequestsRepository.getAllHelpRequestsStream()
            .map { it.size }
            .stateIn(
                scope   = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = 0
            )

    // ── In-memory state ───────────────────────────────────────────────────────

    // Volunteer list is hardcoded — no DB or network needed.
    private val _volunteers = MutableStateFlow(buildVolunteerList())
    val volunteers: StateFlow<List<Volunteer>> = _volunteers.asStateFlow()

    // Student profile — held in memory; survives screen rotations via ViewModel.
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    // Set of volunteer IDs the student has booked in this session.
    private val _bookedVolunteerIds = MutableStateFlow<Set<Int>>(emptySet())
    val bookedVolunteerIds: StateFlow<Set<Int>> = _bookedVolunteerIds.asStateFlow()

    // ── Firestore streams ─────────────────────────────────────────────────────

    // Live stream of community requests that are still Open or being Responded to.
    // callbackFlow inside the repository bridges the Firestore SnapshotListener
    // into a Kotlin Flow; stateIn makes it hot so the UI collects it efficiently.
    val communityRequests: StateFlow<List<CommunityHelpRequest>> =
        communityHelpRepository.observeOpenRequests()
            .map { list -> list.filter { it.status != "Resolved" } }
            .stateIn(
                scope   = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = emptyList()
            )

    // Live stream of requests where THIS user is the responding volunteer.
    // flatMapLatest re-subscribes to Firestore whenever the stored name changes
    // (e.g. the student fills in their profile after launch).
    // distinctUntilChanged prevents redundant re-subscriptions if the name hasn't changed.
    val myResponses: StateFlow<List<CommunityHelpRequest>> =
        _userProfile
            .map { it.studentName }
            .distinctUntilChanged()
            .flatMapLatest { name ->
                // If name is blank the student hasn't set a profile yet — return empty list.
                if (name.isBlank()) flowOf(emptyList())
                else communityHelpRepository.observeMyResponses(name)
            }
            .stateIn(
                scope   = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = emptyList()
            )

    // ── Actions ───────────────────────────────────────────────────────────────

    // Submits a help request.
    // If shareToCommunity is true, it first calls Nominatim via Retrofit to reverse-geocode
    // the GPS coordinates into a human-readable area name, then writes to both Room (local)
    // and Firestore (community board).
    fun submitHelpRequest(
        request: HelpRequest,
        lat: Double? = null,
        lon: Double? = null,
        shareToCommunity: Boolean = false
    ) {
        viewModelScope.launch {
            // Step 1: resolve area name via Retrofit — only for in-person requests (Section 4).
            val areaName = if (shareToCommunity && request.helpType == "In-person" && lat != null && lon != null) {
                locationRepository.getAreaName(lat, lon)
            } else ""

            val finalRequest = request.copy(areaName = areaName)

            // Step 2: always persist locally in Room (Section 3).
            helpRequestsRepository.insertHelpRequest(finalRequest)

            // Step 3: if the student opted in, also post to Firestore (Section 6).
            if (shareToCommunity) {
                val requesterName = when {
                    request.isAnonymous -> "Anonymous"
                    _userProfile.value.studentName.isNotBlank() -> _userProfile.value.studentName
                    else -> "Student"
                }
                try {
                    communityHelpRepository.postRequest(
                        CommunityHelpRequest(
                            subject           = finalRequest.subject,
                            question          = finalRequest.question,
                            areaName          = areaName,
                            isUrgent          = finalRequest.isUrgent,
                            status            = "Open",
                            requesterName     = requesterName,
                            helpType          = request.helpType,
                            contactPreference = request.contactPreference,
                            phoneNumber       = request.phoneNumber
                        )
                    )
                } catch (e: Exception) {
                    // Firestore post failed; local Room save already succeeded so no crash.
                }
            }
        }
    }

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

    // Marks a community request as "Responding" and records the volunteer's name.
    // Calls suspendCancellableCoroutine inside the repository to bridge the Firestore Task.
    fun respondToRequest(id: String, volunteerName: String) {
        viewModelScope.launch {
            try {
                communityHelpRepository.respondToRequest(id, volunteerName)
            } catch (e: Exception) {
                // Network failure — UI stays on the board without crashing.
            }
        }
    }

    // Marks a community request as "Resolved".
    fun resolveRequest(id: String) {
        viewModelScope.launch {
            try {
                communityHelpRepository.resolveRequest(id)
            } catch (e: Exception) {
                // Network failure — card stays in "Responding" state without crashing.
            }
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
                id           = 1,
                name         = "Fahim",
                subjects     = listOf("Math", "Add Maths"),
                availability = "Weekends, 9am–12pm",
                tags         = listOf("Patient", "Bilingual"),
                story        = "I failed Add Maths in Form 4 and retook it. Best decision I made."
            ),
            Volunteer(
                id           = 2,
                name         = "Yasmin",
                subjects     = listOf("English", "Bahasa Malaysia"),
                availability = "Weekdays, 7pm–9pm",
                tags         = listOf("Encouraging", "Direct"),
                story        = "English was my weakest subject. I cried over essays. Now I teach them."
            ),
            Volunteer(
                id           = 3,
                name         = "Chin Wei",
                subjects     = listOf("Chemistry", "Biology"),
                availability = "Weekends, 2pm–5pm",
                tags         = listOf("Energetic"),
                story        = "I almost dropped Science stream. A volunteer changed my mind."
            ),
            Volunteer(
                id           = 4,
                name         = "Arep",
                subjects     = listOf("Physics", "Math"),
                availability = "Weekends, 10am–1pm",
                tags         = listOf("Patient", "Bilingual"),
                story        = "I tutored myself using YouTube for 6 months. I know what it feels like."
            ),
            Volunteer(
                id           = 5,
                name         = "Diana Amira",
                subjects     = listOf("History", "Bahasa Malaysia"),
                availability = "Weekdays, 8pm–10pm",
                tags         = listOf("Encouraging"),
                story        = "History felt pointless to me until someone showed me why it matters."
            ),
            Volunteer(
                id           = 6,
                name         = "Syamil Zaidi",
                subjects     = listOf("Biology", "Chemistry"),
                availability = "Weekends, 3pm–6pm",
                tags         = listOf("Direct"),
                story        = "Failed SPM Biology first try. Passed with A on second. Never give up."
            )
        )
    }
}
