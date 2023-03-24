package com.example.gym

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Routine(
    var name: String,
    var exercises: List<Exercise>,
    var muscleGroups: List<String>
)

data class Exercise(
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
    val screenVal: Int
)
