package com.example.healthexercisetracker.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.healthexercisetracker.data.model.ExerciseSession

@Composable
fun HistoryItem(session: ExerciseSession) {
    Card(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = session.name, style = MaterialTheme.typography.titleSmall)
            Text("Duration: ${session.durationSeconds} sec")
            Text("Distance: %.2f km".format(session.distanceKm))
            Text("Calories: %.1f kcal".format(session.caloriesKcal))
            Text("Speed: %.1f km/h".format(session.avgSpeedKmh))
            Text("HR: ${session.avgHeartRate} bpm")
        }
    }
}
