package com.example.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.local.entity.HabitEntity

@Database(
    entities = [HabitEntity::class],
    version = 5,
)
@TypeConverters(Converter::class)
abstract class HabitDB : RoomDatabase() {
    abstract val dao: HabitDao
}