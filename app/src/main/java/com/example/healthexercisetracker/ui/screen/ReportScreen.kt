package com.example.healthexercisetracker.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CircularProgressIndicator
import com.example.healthexercisetracker.viewmodel.ExerciseViewModel

@Composable
fun ReportScreen(
    viewModel: ExerciseViewModel,
    onNewExercise: () -> Unit
) {
    val summary = viewModel.summary.collectAsState().value

    Box(
        modifier = Modifier
            .padding(8.dp)
            .background(color = Color.White)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (summary == null) {
            CircularProgressIndicator()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Summary", style = MaterialTheme.typography.headlineSmall, color = Color(0xFFFFA500))
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Duration: ${summary.durationSeconds} seconds", color = Color.Black)
                    Text("Average Heart Rate: ${summary.avgHeartRate} bpm", color = Color.Black)
                    Text("Distance: ${"%.2f".format(summary.distanceKm)} km", color = Color.Black)
                    Text("Calories: ${"%.1f".format(summary.caloriesKcal)} kcal", color = Color.Black)
                    Text("Average Speed: ${"%.2f".format(summary.avgSpeedKmh)} km/h", color = Color.Black)
                }

                Button(onClick = onNewExercise) {
                    Text("New Exercise")
                }
            }
        }
    }
}
