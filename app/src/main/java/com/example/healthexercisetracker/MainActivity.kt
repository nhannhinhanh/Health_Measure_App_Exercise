package com.example.healthexercisetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthexercisetracker.service.ExerciseClientManager
import com.example.healthexercisetracker.viewmodel.ExerciseViewModel
import com.example.healthexercisetracker.viewmodel.ExerciseViewModelFactory
import com.example.healthexercisetracker.viewmodel.HistoryViewModel
import com.example.healthexercisetracker.viewmodel.HistoryViewModelFactory
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.healthexercisetracker.ui.screen.ExerciseScreen
import com.example.healthexercisetracker.ui.screen.HistoryScreen
import com.example.healthexercisetracker.ui.screen.ReportScreen
import com.example.healthexercisetracker.ui.screen.StartScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            val context = LocalContext.current

            val exerciseViewModel : ExerciseViewModel = viewModel(
                factory = ExerciseViewModelFactory(context)
            )

            val historyViewModel : HistoryViewModel = viewModel(
                factory = HistoryViewModelFactory(context)
            )

            val exerciseClient = remember { ExerciseClientManager(context) }

            var currentScreen by remember { mutableStateOf("start") }

            when (currentScreen){
                "start" -> StartScreen(
                    viewModel = exerciseViewModel,
                    onStartExercise={currentScreen="exercise"},
                    onOpenHistory={currentScreen="history"}
                )

                "exercise" -> ExerciseScreen(
                    viewModel=exerciseViewModel,
                    client=exerciseClient,
                    onEndExercise = {currentScreen="report"}
                )

                "report" -> ReportScreen(
                    viewModel = exerciseViewModel,
                    onNewExercise = {
                        exerciseViewModel.reset()
                        currentScreen="start"
                    }
                )

                "history" -> HistoryScreen (
                    viewModel= historyViewModel,
                    onBack={
                        currentScreen="start"
                    }
                )
            }
        }
    }
}
