package com.example.a213396_lingchinwei_drnazatulaini_project1.ui

// This is the Match screen (Find a Mentor) — shows the list of volunteer mentors.
// The student can filter by subject category and book a session with any mentor.
// Volunteers are pre-loaded in the ViewModel — no internet or database needed.

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.a213396_lingchinwei_drnazatulaini_project1.BottomNavigationBar
import com.example.a213396_lingchinwei_drnazatulaini_project1.data.Volunteer
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AtlasBlue
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AtlasNavy
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AtlasSky
import com.example.a213396_lingchinwei_drnazatulaini_project1.ui.theme.AtlasText

@Composable
fun MatchScreen(navController: NavHostController, viewModel: AtlasViewModel) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Read the full volunteer list and the set of booked volunteer IDs from the ViewModel
    val volunteerList      by viewModel.volunteers.collectAsState()
    val bookedVolunteerIds by viewModel.bookedVolunteerIds.collectAsState()

    // searchQuery holds what the user is currently typing in the TextField
    var searchQuery by remember { mutableStateOf("") }

    // submittedQuery only updates when the user presses the Search button
    // This is what drives the actual filtering — not every keystroke
    var submittedQuery by remember { mutableStateOf("") }

    // The currently selected filter chip — "All" means show every volunteer
    var selectedFilter by remember { mutableStateOf("All") }

    // Step 1: apply chip filter
    val chipFilteredVolunteers = if (selectedFilter == "All") {
        volunteerList
    } else {
        volunteerList.filter { volunteer ->
            volunteer.subjects.any { subject ->
                when (selectedFilter) {
                    "Math"      -> subject == "Math" || subject == "Add Maths" || subject == "Physics"
                    "Science"   -> subject == "Chemistry" || subject == "Biology" || subject == "Science"
                    "Languages" -> subject == "English" || subject == "Bahasa Malaysia" || subject == "History"
                    else        -> false
                }
            }
        }
    }

    // Step 2: apply text search on top of chip filter (only if the user has submitted a query)
    val filteredVolunteers = if (submittedQuery.isBlank()) {
        chipFilteredVolunteers
    } else {
        chipFilteredVolunteers.filter { volunteer ->
            // Check if any of this mentor's subjects contain the typed text
            volunteer.subjects.any { subject ->
                subject.contains(submittedQuery, ignoreCase = true)
            }
        }
    }

    val filterOptions = listOf("All", "Math", "Science", "Languages")

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
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text       = "Find a Mentor",
                        fontSize   = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color      = Color.White
                    )
                    Text(
                        text     = "All mentors are verified Tutors in Action volunteers. 100% free.",
                        fontSize = 13.sp,
                        color    = AtlasSky
                    )
                }
            }

            // Search bar: TextField + Button (Lab 2 requirement)
            MentorSearchBar(
                searchQuery    = searchQuery,
                onQueryChange  = { searchQuery = it },
                onSearchClick  = { submittedQuery = searchQuery }
            )

            // Show a result message when a search is active
            if (submittedQuery.isNotBlank()) {
                val resultCount = filteredVolunteers.size
                val resultMessage = if (resultCount > 0)
                    "Showing $resultCount mentor${if (resultCount != 1) "s" else ""} who teach \"$submittedQuery\""
                else
                    "No mentors found for \"$submittedQuery\""
                Text(
                    text     = resultMessage,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color    = if (resultCount > 0) AtlasBlue else Color(0xFFE53935),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            // Filter chips to narrow down the volunteer list by subject category
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filterOptions.forEach { filterOption ->
                    FilterChip(
                        selected = selectedFilter == filterOption,
                        onClick  = { selectedFilter = filterOption },
                        label    = { Text(filterOption) },
                        colors   = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AtlasNavy,
                            selectedLabelColor     = AtlasSky
                        )
                    )
                }
            }

            // Scrollable list of volunteer cards, or a "no results" message
            if (filteredVolunteers.isEmpty()) {
                Column(
                    modifier            = Modifier.fillMaxSize().padding(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.PersonSearch,
                        contentDescription = null,
                        tint     = AtlasNavy.copy(alpha = 0.3f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text       = "No mentors match your search.",
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = AtlasNavy.copy(alpha = 0.6f)
                    )
                }
            } else {
                LazyColumn(
                    contentPadding      = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredVolunteers) { volunteer ->
                        VolunteerCard(
                            volunteer     = volunteer,
                            isBooked      = volunteer.id in bookedVolunteerIds,
                            onBookSession = { viewModel.bookVolunteer(volunteer.id) }
                        )
                    }
                }
            }
        }
    }
}

// The search bar with a TextField and a Search button.
// searchQuery updates every time the user types (live state).
// submittedQuery only changes when the user presses Search — that is what filters the list.
@Composable
fun MentorSearchBar(
    searchQuery:   String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    Row(
        modifier             = Modifier
            .fillMaxWidth()
            .background(AtlasNavy)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment    = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // TextField — the user types a subject name here
        OutlinedTextField(
            value         = searchQuery,
            onValueChange = onQueryChange,
            placeholder   = { Text("Search by subject...", color = AtlasText.copy(alpha = 0.45f), fontSize = 14.sp) },
            singleLine    = true,
            modifier      = Modifier.weight(1f),
            shape         = RoundedCornerShape(12.dp),
            colors        = OutlinedTextFieldDefaults.colors(
                focusedBorderColor      = AtlasSky,
                unfocusedBorderColor    = AtlasText.copy(alpha = 0.3f),
                focusedTextColor        = AtlasText,
                unfocusedTextColor      = AtlasText,
                cursorColor             = AtlasSky,
                focusedContainerColor   = AtlasNavy,
                unfocusedContainerColor = AtlasNavy
            )
        )
        // Button — pressing this applies the search and updates the displayed list
        Button(
            onClick = onSearchClick,
            shape   = RoundedCornerShape(12.dp),
            colors  = ButtonDefaults.buttonColors(containerColor = AtlasBlue),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White, modifier = Modifier.size(22.dp))
        }
    }
}

// A card showing one volunteer mentor's information — tap to expand/collapse details
@Composable
fun VolunteerCard(volunteer: Volunteer, isBooked: Boolean, onBookSession: () -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(300))
            .clickable { isExpanded = !isExpanded },
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Always visible: avatar, name, and organisation badge
            VolunteerHeader(volunteer = volunteer)

            // Always visible: color-coded subject chips
            Row(
                modifier              = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                volunteer.subjects.forEach { subject ->
                    val chipColor = getSubjectColor(subject)
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = chipColor.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text       = subject,
                            modifier   = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontSize   = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color      = chipColor
                        )
                    }
                }
            }

            // Always visible: expand/collapse hint row
            Row(
                modifier             = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment    = Alignment.CenterVertically
            ) {
                Text(
                    text     = if (isExpanded) "Hide details" else "Tap to see details",
                    fontSize = 12.sp,
                    color    = AtlasNavy.copy(alpha = 0.45f)
                )
                Icon(
                    imageVector        = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint               = AtlasNavy.copy(alpha = 0.45f),
                    modifier           = Modifier.size(20.dp)
                )
            }

            // Expandable section — availability, tags, story, and book button
            AnimatedVisibility(
                visible = isExpanded,
                enter   = expandVertically(animationSpec = tween(300)),
                exit    = shrinkVertically(animationSpec = tween(300))
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    HorizontalDivider(color = AtlasNavy.copy(alpha = 0.08f))

                    // Availability schedule
                    Row(
                        verticalAlignment    = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = null,
                            tint     = AtlasNavy.copy(alpha = 0.5f),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text     = volunteer.availability,
                            fontSize = 13.sp,
                            color    = AtlasNavy.copy(alpha = 0.7f)
                        )
                    }

                    // Personality tags (Patient, Bilingual, etc.)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        volunteer.tags.forEach { tag ->
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = AtlasNavy.copy(alpha = 0.08f)
                            ) {
                                Text(
                                    text     = tag,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    fontSize = 11.sp,
                                    color    = AtlasNavy.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }

                    // The mentor's personal failure-and-recovery story
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(8.dp),
                        colors   = CardDefaults.cardColors(containerColor = Color(0xFFF0F4FF))
                    ) {
                        Row(
                            modifier             = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.FormatQuote,
                                contentDescription = null,
                                tint     = AtlasBlue.copy(alpha = 0.5f),
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text       = volunteer.story,
                                fontSize   = 13.sp,
                                color      = AtlasNavy,
                                lineHeight = 19.sp,
                                fontStyle  = FontStyle.Italic
                            )
                        }
                    }

                    // Book Session button — turns into "Booked!" after tapping
                    Button(
                        onClick  = onBookSession,
                        enabled  = !isBooked,
                        modifier = Modifier.fillMaxWidth(),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = ButtonDefaults.buttonColors(
                            containerColor         = AtlasBlue,
                            disabledContainerColor = AtlasNavy.copy(alpha = 0.2f)
                        )
                    ) {
                        Icon(
                            imageVector        = if (isBooked) Icons.Default.CheckCircle else Icons.Default.BookOnline,
                            contentDescription = null,
                            modifier           = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text       = if (isBooked) "Session Booked!" else "Book Session",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// The top section of a volunteer card: circle with initial, name, and org badge
@Composable
fun VolunteerHeader(volunteer: Volunteer) {
    Row(
        verticalAlignment    = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Circle avatar showing the first letter of the mentor's name
        Box(
            modifier         = Modifier
                .size(44.dp)
                .background(AtlasBlue.copy(alpha = 0.15f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text       = volunteer.name.first().toString(),
                fontSize   = 18.sp,
                fontWeight = FontWeight.Bold,
                color      = AtlasBlue
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = volunteer.name,
                fontSize   = 16.sp,
                fontWeight = FontWeight.Bold,
                color      = AtlasNavy
            )
            // Organisation badge with a verified tick
            Row(
                verticalAlignment    = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Icon(
                    Icons.Default.Verified,
                    contentDescription = null,
                    tint     = AtlasBlue,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text     = volunteer.organisation,
                    fontSize = 11.sp,
                    color    = AtlasBlue
                )
            }
        }
    }
}
