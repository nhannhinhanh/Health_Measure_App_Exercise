package com.example.healthexercisetracker.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.healthexercisetracker.data.model.ExerciseSession
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: ExerciseSession)

    @Query("SELECT * FROM exercise_sessions ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<ExerciseSession>>
}
