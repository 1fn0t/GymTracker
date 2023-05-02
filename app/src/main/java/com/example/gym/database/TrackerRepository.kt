package com.example.gym.database

import com.example.gym.Exercise
import com.example.gym.Routine
import javax.inject.Inject

class TrackerRepository @Inject constructor(
    private val trackerDatabaseDao: TrackerDatabaseDao
) {
    val readAllExerciseData = trackerDatabaseDao.getAllExercises()
    val readAllRoutineData = trackerDatabaseDao.getAllRoutines()

    suspend fun addExercise(item: Exercise) {
        trackerDatabaseDao.insert(
            ExerciseItem(
                name = item.name,
                muscles = item.muscleGroups
            )
        )
    }

    suspend fun updateExercise(item: Exercise) {
        trackerDatabaseDao.update(
            ExerciseItem(
                name = item.name,
                muscles = item.muscleGroups
            )
        )
    }

    suspend fun deleteExercise(item: Exercise) {
        trackerDatabaseDao.delete(
            ExerciseItem(
                name = item.name,
                muscles = item.muscleGroups
            )
        )
    }

    suspend fun addRoutine(item: Routine) {
        trackerDatabaseDao.insert(
            RoutineItem(
                name = item.name,
                exercises = item.exercises,
                muscles = item.muscleGroups
            )
        )
    }

    suspend fun updateRoutine(item: Routine) {
        trackerDatabaseDao.update(
            RoutineItem(
                name = item.name,
                exercises = item.exercises,
                muscles = item.muscleGroups
            )
        )
    }

    suspend fun deleteRoutine(item: Routine) {
        trackerDatabaseDao.delete(
            RoutineItem(
                name = item.name,
                exercises = item.exercises,
                muscles = item.muscleGroups
            )
        )
    }
}