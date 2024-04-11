package com.example.habittracker.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.habittracker.data.models.Habit

@Database(
    entities = [Habit::class],
    version = 1
)
@TypeConverters(HabitTypeConverter::class, UUIDConverter::class)
abstract class HabitDB : RoomDatabase() {
    abstract fun getDao():HabitDao

    companion object {
        fun getHabitDB(context: Context): HabitDB {
            return Room.databaseBuilder(
                context.applicationContext,
                HabitDB::class.java,
                "Habit.db"
            ).allowMainThreadQueries().build()
        }
    }
}