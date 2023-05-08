package com.example.gym.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.gym.Exercise
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

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

    @PrimaryKey
    @ColumnInfo(name = "routine_name")
    var name: String,

    @ColumnInfo(name = "exercises_in_routine")
    @TypeConverters(ExerciseConverters::class)
    val exercises: List<Exercise>,

    @ColumnInfo(name = "muscles_targeted")
    @TypeConverters(StringConverters::class)
    val muscles: List<String>
)

@Entity(tableName = "sessions")
data class SessionItem(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "routine_name")
    val routineName: String,

    @ColumnInfo(name = "rep_counts")
    @TypeConverters(IntegerConverters::class)
    val repCounts: List<Int>,

    @ColumnInfo(name = "date_created")
    @TypeConverters(DateConverters::class)
    val dateCreated: LocalDateTime
)