package com.example.gym.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackerDatabaseDao {

    companion object {
        private const val EXERCISE_TABLE = "exercises"
        private const val ROUTINE_TABLE = "routines"
    }

    @Query("SELECT * from $EXERCISE_TABLE")
    fun getAllExercises(): Flow<List<ExerciseItem>>

    @Insert
    suspend fun insert(item: ExerciseItem)

    @Update
    suspend fun update(item: ExerciseItem)

    @Delete
    suspend fun delete(item: ExerciseItem)

    @Query("SELECT * from $ROUTINE_TABLE")
    fun getAllRoutines(): Flow<List<RoutineItem>>

    @Insert
    suspend fun insert(item: RoutineItem)

    @Update
    suspend fun update(item: RoutineItem)

    @Delete
    suspend fun delete(item: RoutineItem)
}