package com.example.gym

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gym.database.TrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel  @Inject constructor(private val trackerRepo: TrackerRepository): ViewModel() {

    fun retrieveRoutinesFromDB(): Flow<List<Routine>> {
        return trackerRepo.readAllRoutineData.map { routineItems ->
            routineItems.map { routineItem ->
                Routine(
                    name = routineItem.name,
                    exercises = routineItem.exercises,
                    muscleGroups = routineItem.muscles
                )
            }
        }
    }

    fun storeRoutineInDB(name: String, exercises: List<Exercise>, muscleGroups: List<String>) {
        viewModelScope.launch {
            trackerRepo.addRoutine(
                Routine(
                    name = name,
                    exercises = exercises,
                    muscleGroups = muscleGroups
                )
            )
        }
    }

    fun deleteRoutineInDB(routine: Routine) {
        viewModelScope.launch {
            trackerRepo.deleteRoutine(routine)
        }
    }

    fun retrieveExercisesFromDB(): Flow<List<Exercise>> {
        return trackerRepo.readAllExerciseData.map { exerciseItems ->
            exerciseItems.map { exerciseItem ->
                Exercise(
                    name = exerciseItem.name,
                    muscleGroups = exerciseItem.muscles
                )
            }
        }
    }

    fun storeExerciseInDB(name: String, muscleGroups: List<String>) {
        viewModelScope.launch {
            trackerRepo.addExercise(
                Exercise(
                    name = name,
                    muscleGroups = muscleGroups
                )
            )
        }
    }

    fun deleteExerciseInDB(exercise: Exercise) {
        viewModelScope.launch {
            trackerRepo.deleteExercise(
                exercise
            )
        }
    }

}