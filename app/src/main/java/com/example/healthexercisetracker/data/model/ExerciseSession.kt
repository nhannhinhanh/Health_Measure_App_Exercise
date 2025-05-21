package com.example.healthexercisetracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_sessions")
data class ExerciseSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val name: String,
    val durationSeconds: Long,
    val avgHeartRate: Int,
    val distanceKm: Double,
    val caloriesKcal: Double,
    val avgSpeedKmh: Double,

    val timestamp: Long = System.currentTimeMillis() // to sort or display date
)
