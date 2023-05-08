package com.example.gym.routines

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.gym.Exercise

class MuscleViewModel: ViewModel() {
    private val _muscles: SnapshotStateList<String> = mutableStateListOf()
    val muscles: List<String>
        get() = _muscles

    fun addMuscle(muscle: String) {
        _muscles.add(muscle)
    }
    fun removeMuscle(muscle: String) {
        _muscles.remove(muscle)
    }
    fun clearMuscles() {
        _muscles.clear()
    }
}
class ExerciseViewModel: ViewModel() {
    private val _exercises: SnapshotStateList<Exercise> = mutableStateListOf()
    val exercises: List<Exercise>
        get() = _exercises
    fun addExercise(exercise: Exercise) {
        _exercises.add(exercise)
    }
    fun addAllExercises(exercises: List<Exercise>) {
        _exercises.addAll(exercises)
    }
    fun removeExercise(exercise: Exercise) {
        _exercises.remove(exercise)
    }
    fun clearExercises() {
        _exercises.clear()
    }
}