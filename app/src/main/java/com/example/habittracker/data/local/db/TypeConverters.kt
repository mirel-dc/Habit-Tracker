package com.example.habittracker.data.local.db

import androidx.room.TypeConverter
import com.example.habittracker.data.local.entity.HabitType
import java.util.UUID

class HabitTypeConverter {
    @TypeConverter
    fun fromHabitType(habitType: HabitType): Int {
        return habitType.value
    }

    @TypeConverter
    fun toHabitType(value: Int): HabitType {
        return HabitType.getHabitTypeByValue(value)
    }
}

class UUIDConverter {
    @TypeConverter
    fun fromUUID(value: UUID): String = value.toString()

    @TypeConverter
    fun toUUID(value: String): UUID = UUID.fromString(value)
}