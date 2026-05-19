package com.example.a213396_lingchinwei_drnazatulaini_lab5.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "help_requests")
data class HelpRequest(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val subject: String,
    val question: String,
    val isUrgent: Boolean,
    val isAnonymous: Boolean,
    val contactPreference: String,
    val status: String = "Pending",
    val timestamp: Long = System.currentTimeMillis()
)


