package com.example.healthexercisetracker.data.model

data class ExerciseSummary(
    val durationSeconds: Long,
    val avgHeartRate: Int,
    val distanceKm: Double,
    val caloriesKcal: Double,
    val avgSpeedKmh: Double
)
