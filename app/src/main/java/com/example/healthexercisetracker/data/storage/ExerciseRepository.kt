package com.example.healthexercisetracker.data.storage

import com.example.healthexercisetracker.data.model.ExerciseSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExerciseRepository(private val dao: ExerciseDao) {

    suspend fun insertSession(session: ExerciseSession) {
        withContext(Dispatchers.IO) {
            dao.insertSession(session)
        }
    }

    fun getAllSessions() = dao.getAllSessions()
}
