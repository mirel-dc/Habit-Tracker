package com.example.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.entity.HabitEntity

@Database(
    entities = [HabitEntity::class],
    version = 4,
)
abstract class HabitDB : RoomDatabase() {
    abstract val dao: HabitDao
}