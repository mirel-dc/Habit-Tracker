package com.example.data.local.db

import androidx.room.TypeConverter
import com.example.domain.model.HabitType
import java.util.UUID
import androidx.room.ProvidedTypeConverter
import com.google.gson.JsonParser

//class HabitTypeConverter {
//    @TypeConverter
//    fun fromHabitType(habitType: HabitType): Int {
//        return habitType.value
//    }
//
//    @TypeConverter
//    fun toHabitType(value: Int): HabitType {
//        return HabitType.getHabitTypeByValue(value)
//    }
//}

class UUIDConverter {
    @TypeConverter
    fun fromUUID(value: UUID): String = value.toString()

    @TypeConverter
    fun toUUID(value: String): UUID = UUID.fromString(value)
}