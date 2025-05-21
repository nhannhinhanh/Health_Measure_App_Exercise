package com.example.healthexercisetracker.service

import android.content.Context
import androidx.annotation.OptIn
import androidx.health.services.client.HealthServices
import androidx.health.services.client.ExerciseClient
import androidx.health.services.client.clearUpdateCallback
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.ExerciseConfig
import androidx.health.services.client.data.ExerciseType
import androidx.health.services.client.data.ExerciseUpdate
import androidx.health.services.client.data.ExerciseState
import androidx.health.services.client.ExerciseUpdateCallback
import androidx.health.services.client.data.Availability
import androidx.health.services.client.data.CumulativeDataPoint
import androidx.health.services.client.data.ExerciseLapSummary
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class ExerciseClientManager(context: Context) {
    private val exerciseClient: ExerciseClient = HealthServices.getClient(context).exerciseClient

    private val _exerciseState = MutableStateFlow<ExerciseState?>(null)
    val exerciseState: StateFlow<ExerciseState?> = _exerciseState.asStateFlow()

    private val _heartRate = MutableStateFlow<Float?>(null)
    val heartRate: StateFlow<Float?> = _heartRate.asStateFlow()

    private val _distance = MutableStateFlow<Double?>(null)
    val distance: StateFlow<Double?> = _distance.asStateFlow()

    private val _calories = MutableStateFlow<Double?>(null)
    val calories: StateFlow<Double?> = _calories.asStateFlow()

    private val _speed = MutableStateFlow<Float?>(null)
    val speed: StateFlow<Float?> = _speed.asStateFlow()

    private var simulationJob: Job? = null

    private val exerciseUpdateCallback = object : ExerciseUpdateCallback {
        override fun onExerciseUpdateReceived(update: ExerciseUpdate) {
            _exerciseState.value = update.exerciseStateInfo.state

            update.latestMetrics.getData(DataType.HEART_RATE_BPM).firstOrNull()?.let {
                _heartRate.value = it.value.toFloat()
            }
            val distancePoint: CumulativeDataPoint<Double>? =
                update.latestMetrics.getData(DataType.DISTANCE_TOTAL)
            _distance.value = distancePoint?.total?.toDouble()

            val caloriesPoint: CumulativeDataPoint<Double>? =
                update.latestMetrics.getData(DataType.CALORIES_TOTAL)
            _calories.value = caloriesPoint?.total?.toDouble()

            update.latestMetrics.getData(DataType.SPEED)
                .firstOrNull()?.let { point ->
                    _speed.value = point.value.toFloat()
                }
        }

        override fun onRegistered() {}

        override fun onRegistrationFailed(throwable: Throwable) {
            _exerciseState.value = ExerciseState.ENDED
        }

        override fun onLapSummaryReceived(lapSummary: ExerciseLapSummary) {}

        override fun onAvailabilityChanged(dataType: DataType<*, *>, availability: Availability) {}
    }

    suspend fun startExercise(exerciseType: ExerciseType, useSimulation: Boolean = false) {
        val exerciseConfig = ExerciseConfig.Builder(exerciseType)
            .setDataTypes(setOf(
                DataType.HEART_RATE_BPM,
                DataType.DISTANCE_TOTAL,
                DataType.CALORIES_TOTAL,
                DataType.SPEED
            ))
            .build()

        try {
            exerciseClient.setUpdateCallback(exerciseUpdateCallback)
            exerciseClient.startExerciseAsync(exerciseConfig).await()
            _exerciseState.value = ExerciseState.ACTIVE

            if (useSimulation) startSimulatedMetrics()
        } catch (e: Exception) {
            _exerciseState.value = ExerciseState.ENDED
            e.printStackTrace()
        } finally {
            if (useSimulation) startSimulatedMetrics()
        }
    }

    @OptIn(UnstableApi::class)
    private fun startSimulatedMetrics() {
        var totalDistance = 0.0
        var totalCalories = 0.0
        var currentSpeed = 4.0f

        simulationJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                val hr = Random.nextInt(90, 130)
                val delta = Random.nextFloat() * 0.4f - 0.2f
                currentSpeed = (currentSpeed + delta).coerceIn(3.0f, 5.0f)

                val distanceStep = currentSpeed * 1  // m/s over 1 second
                val kcalStep = 0.03 * currentSpeed

                totalDistance += distanceStep
                totalCalories += kcalStep

                _heartRate.value = hr.toFloat()
                _speed.value = currentSpeed
                _distance.value = totalDistance
                _calories.value = totalCalories

                Log.d("Simulator", "HR=$hr, Speed=$currentSpeed, Dist=$totalDistance, Kcal=$totalCalories")

                delay(1000)
            }
        }
    }

    suspend fun endExercise() {
        try {
            exerciseClient.endExerciseAsync().await()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            exerciseClient.clearUpdateCallback(exerciseUpdateCallback)
            _exerciseState.value = ExerciseState.ENDED
            simulationJob?.cancel()
        }
    }
}
