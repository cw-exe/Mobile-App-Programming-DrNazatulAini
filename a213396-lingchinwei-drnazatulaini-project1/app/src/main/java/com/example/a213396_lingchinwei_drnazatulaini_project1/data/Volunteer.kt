package com.example.a213396_lingchinwei_drnazatulaini_project1.data

// A Volunteer is a mentor from Tutors in Action Malaysia.
// Each volunteer has subjects they can teach, when they are free, and a short personal story.
data class Volunteer(
    val id: Int,
    val name: String,
    val subjects: List<String>,
    val availability: String,
    val tags: List<String>,
    val story: String,
    val organisation: String = "Tutors in Action Malaysia"
)
