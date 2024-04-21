package com.example.smartforks.model

data class UserPreferences(
    val height: String,                // Height in cm
    val weight: String,                // Weight in kg
    val dietaryPref: String?,       // Dietary preferences
    val allergies: String?,         // Any allergies
    val goal: String?               // Fitness goal
)