package com.example.gym.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.gym.Exercise

@Entity(tableName = "exercises")
data class ExerciseItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "content")
    var name: String,

    @ColumnInfo(name = "muscles_targeted")
    @TypeConverters(StringConverters::class)
    val muscles: List<String>
)

@Entity(tableName = "routines")
data class RoutineItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "routine_name")
    var name: String,

    @ColumnInfo(name = "exercises_in_routine")
    @TypeConverters(ExerciseConverters::class)
    val exercises: List<Exercise>,

    @ColumnInfo(name = "muscles_targeted")
    @TypeConverters(StringConverters::class)
    val muscles: List<String>
)