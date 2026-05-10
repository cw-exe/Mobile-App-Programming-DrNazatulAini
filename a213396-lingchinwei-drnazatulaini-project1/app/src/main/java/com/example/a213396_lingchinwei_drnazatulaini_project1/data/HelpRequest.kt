package com.example.a213396_lingchinwei_drnazatulaini_project1.data

// A HelpRequest is one question a student has asked for help with.
// Each request has a subject, a question, and settings like urgency and anonymity.
data class HelpRequest(
    val id: Int,
    val subject: String,
    val question: String,
    val isUrgent: Boolean,
    val isAnonymous: Boolean,
    val contactPreference: String,
    val status: String = "Pending",
    val timestamp: Long = System.currentTimeMillis()
)
