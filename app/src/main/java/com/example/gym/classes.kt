package com.example.gym

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.gym.database.MuscleType
import com.example.gym.navigation.Screen
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


data class Routine(
    var name: String,
    var exercises: List<Exercise>,
    var muscleGroups: List<String>
)
data class Exercise(
//    var id: Long,
    var name: String,
    var muscleGroups: List<String>,
)

data class MuscleGroup (
    val list: List<String>,
    val type: MuscleType,
)

data class NavIcon(
    @DrawableRes val icon: Int,
    @StringRes val info: Int,
    val screenVal: Screen,
)

data class SessionEntry(
    val routineName: String,
    val repCounts: List<Int>,
    val dateCreated: LocalDateTime,
)

data class SessionStatistic(
    val routineName: String,
    val exerciseStatistics: MutableList<ExerciseStatistic>,
)

data class ExerciseStatistic(
    val exerciseName: String,
    val repCounts: MutableList<Float>,
)
