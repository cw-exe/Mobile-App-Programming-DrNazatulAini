package com.example.a213396_lingchinwei_drnazatulaini_project2.data

import com.google.firebase.firestore.DocumentId

// All fields have defaults so Firestore's reflection-based deserialization can use the no-arg constructor.
// @DocumentId populates `id` from the document ID on reads and excludes it from writes.
data class CommunityHelpRequest(
    @DocumentId val id: String = "",
    val subject: String = "",
    val question: String = "",
    val areaName: String = "",
    val isUrgent: Boolean = false,
    val status: String = "Open",        // "Open" → "Responding" → "Resolved"
    val requesterName: String = "",
    val respondedBy: String = "",
    val helpType: String = "Online",          // "Online" | "In-person" — controls GPS/area display
    val contactPreference: String = "Chat",   // "Chat" | "Voice Call" — how student wants to be reached
    val phoneNumber: String = "",             // optional direct contact; blank when anonymous
    val timestamp: Long = System.currentTimeMillis()
)
