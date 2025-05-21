package com.example.healthexercisetracker.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.wear.compose.material.Text
import com.example.healthexercisetracker.ui.component.HistoryItem
import com.example.healthexercisetracker.viewmodel.HistoryViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx. wear. compose. material. ButtonDefaults

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val sessions by viewModel.sessions.collectAsState(initial = emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF708090))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                if (sessions.isEmpty()) {
                    item {
                        Text("No sessions yet", color = Color.Red)
                    }
                } else {
                    items(sessions) { session ->
                        HistoryItem(session)
                    }
                }
            }

            Button(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFA500)) // Soft blue
            ) {
                Text("Back", color = Color.White)
            }
        }
    }
}
