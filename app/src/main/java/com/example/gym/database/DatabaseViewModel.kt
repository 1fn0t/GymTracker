package com.example.gym.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gym.Exercise
import com.example.gym.Routine
import com.example.gym.SessionEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*
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

//    fun retrieveRoutineById(id: String): Routine {
//        val routineItem = trackerRepo.readRoutineById(id)
//        return Routine(
//            name = routineItem.name,
//            exercises = routineItem.exercises,
//            muscleGroups = routineItem.muscles
//        )
//    }
    suspend fun retrieveRoutineByName(id: String): Routine {
        val routineItem = trackerRepo.readRoutineByName(id)
        return Routine(
            name = routineItem.name,
            exercises = routineItem.exercises,
            muscleGroups = routineItem.muscles
        )
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

    fun updateRoutineInDB(routine: Routine) {
        viewModelScope.launch {
            trackerRepo.updateRoutine(routine)
        }
    }

    fun storeSessionEntryInDB(entry: SessionEntry) {
        viewModelScope.launch {
            trackerRepo.addSessionEntry(entry)
        }
    }

    fun retrieveMostRecentSessionEntriesFromDB(): Flow<List<SessionEntry>> {
        return trackerRepo.readMostRecentSessionEntries.map { sessions ->
            sessions.map {
                SessionEntry(
                    routineName = it.routineName,
                    repCounts = it.repCounts,
                    dateCreated = it.dateCreated
                )
            }
        }
    }

    fun retrieveSessionsWithinMonthFromDB(): Flow<List<SessionEntry>> {
        return trackerRepo.readSessionsWithinMonth.map { sessions ->
            sessions.map {
                SessionEntry(
                    routineName = it.routineName,
                    repCounts = it.repCounts,
                    dateCreated = it.dateCreated
                )
            }
        }
    }
}