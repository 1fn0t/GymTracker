package com.example.gym.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ExerciseItem::class, RoutineItem::class], version = 1, exportSchema = false)
@TypeConverters(StringConverters::class, ExerciseConverters::class)
abstract class TrackerDatabase : RoomDatabase() {
    abstract fun trackerDao(): TrackerDatabaseDao

    companion object {
        private var INSTANCE: TrackerDatabase? = null

        fun getInstance(context: Context): TrackerDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TrackerDatabase::class.java,
                        "tracker_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}