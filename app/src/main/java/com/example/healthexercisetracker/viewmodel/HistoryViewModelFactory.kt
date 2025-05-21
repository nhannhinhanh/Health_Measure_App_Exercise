package com.example.healthexercisetracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.healthexercisetracker.data.storage.ExerciseDatabase
import com.example.healthexercisetracker.data.storage.ExerciseRepository

class HistoryViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val repository by lazy {
        val dao = ExerciseDatabase.getInstance(context).exerciseDao()
        ExerciseRepository(dao)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryViewModel(repository) as T
    }
}
