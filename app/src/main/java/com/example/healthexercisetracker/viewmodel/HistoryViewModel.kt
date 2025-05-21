package com.example.healthexercisetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthexercisetracker.data.model.ExerciseSession
import com.example.healthexercisetracker.data.storage.ExerciseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(private val repository: ExerciseRepository) : ViewModel() {

    val sessions: StateFlow<List<ExerciseSession>> = repository
        .getAllSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
