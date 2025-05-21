package com.example.healthexercisetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthexercisetracker.data.model.ExerciseSession
import com.example.healthexercisetracker.data.model.ExerciseSummary
import com.example.healthexercisetracker.data.storage.ExerciseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExerciseViewModel(
    private val repository: ExerciseRepository
) : ViewModel() {

    var exerciseName: String = "Exercise 1"
    var durationSeconds: Long = 0L
    var avgHeartRate: Int = 95
    var distanceKm: Double = 2.03
    var caloriesKcal: Double = 143.7
    var avgSpeedKmh: Double = 7.2

    private val _summary = MutableStateFlow<ExerciseSummary?>(null)
    val summary: StateFlow<ExerciseSummary?> = _summary.asStateFlow()

    fun endExerciseAndStore() {
        val session = ExerciseSession(
            name = exerciseName,
            durationSeconds = durationSeconds,
            avgHeartRate = avgHeartRate,
            distanceKm = distanceKm,
            caloriesKcal = caloriesKcal,
            avgSpeedKmh = avgSpeedKmh
        )

        val report = ExerciseSummary(
            durationSeconds = durationSeconds,
            avgHeartRate = avgHeartRate,
            distanceKm = distanceKm,
            caloriesKcal = caloriesKcal,
            avgSpeedKmh = avgSpeedKmh
        )

        viewModelScope.launch {
            repository.insertSession(session)
            _summary.value = report
        }
    }

    fun reset() {
        _summary.value = null
    }
}
