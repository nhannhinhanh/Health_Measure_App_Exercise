package com.example.healthexercisetracker.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.health.services.client.data.ExerciseType
import androidx.wear.compose.material.*
import com.example.healthexercisetracker.service.ExerciseClientManager
import com.example.healthexercisetracker.utils.TimerManager
import com.example.healthexercisetracker.viewmodel.ExerciseViewModel
import kotlinx.coroutines.delay

fun formatDuration(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60

    return if (hours > 0) {
        "%d:%02d:%02d".format(hours, minutes, secs)
    } else {
        "%02d:%02d".format(minutes, secs)
    }
}

@Composable
fun ExerciseScreen(
    viewModel: ExerciseViewModel,
    client: ExerciseClientManager,
    onEndExercise: () -> Unit
) {
    val timer = remember { TimerManager() }

    val time by timer.elapsedTime.collectAsState()
    val heartRate by client.heartRate.collectAsState()
    val distance by client.distance.collectAsState()
    val calories by client.calories.collectAsState()
    val speed by client.speed.collectAsState()

    var paused by remember { mutableStateOf(false) }
    var metricsVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        client.startExercise(ExerciseType.RUNNING_TREADMILL, useSimulation = true)
        timer.start()
        delay(500)
        metricsVisible = true
    }

    Scaffold(
        timeText = { TimeText() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF708090))
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = viewModel.exerciseName,
                    style = MaterialTheme.typography.title3,
                    color = Color(0xFFFFA500)
                )

                Text(
                    text = formatDuration(time / 1000),
                    style = MaterialTheme.typography.display1,
                    color = Color(0xFFFFA500) // Xanh dương sáng
                )

                AnimatedVisibility(
                    visible = metricsVisible,
                    enter = slideInVertically() + fadeIn()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        MetricRow(label = "Heart Rate", value = "${heartRate ?: "--"} bpm", icon = "❤️‍🔥️")
                        MetricRow(label = "Calories", value = "%.1f kcal".format(calories ?: 0.0), icon = "💪🏻")
                        MetricRow(label = "Distance", value = "%.2f km".format((distance ?: 0.0) / 1000), icon = "📏")
                        MetricRow(label = "Speed", value = "%.1f km/h".format((speed ?: 0f) * 3.6f), icon = "⚡️")
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            paused = !paused
                            if (paused) timer.pause() else timer.resume()
                        },
                        modifier = Modifier.size(48.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF90CAF9)) // xanh dương nhạt
                    ) {
                        Icon(
                            imageVector = if (paused) Icons.Default.PlayArrow else Icons.Default.Pause,
                            contentDescription = if (paused) "Resume" else "Pause",
                            tint = Color.White
                        )
                    }

                    Button(
                        onClick = {
                            val totalSeconds = timer.stop()
                            viewModel.durationSeconds = totalSeconds
                            viewModel.avgHeartRate = heartRate?.toInt() ?: 0
                            viewModel.distanceKm = distance ?: 0.0
                            viewModel.caloriesKcal = calories ?: 0.0
                            viewModel.avgSpeedKmh = speed?.toDouble() ?: 0.0
                            viewModel.endExerciseAndStore()
                            onEndExercise()
                        },
                        modifier = Modifier.size(48.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "End",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MetricRow(label: String, value: String, icon: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$icon $label", fontSize = 10.sp, color = Color.DarkGray)
        Text(value, fontSize = 10.sp, color = Color.Black)
    }
}

