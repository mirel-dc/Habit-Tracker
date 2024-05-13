package com.example.habittracker.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.habittracker.R
import com.example.habittracker.data.remote.dto.HabitDTO
import kotlinx.parcelize.Parcelize
import java.util.Calendar
import java.util.UUID

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
    var editDate: Long = Calendar.getInstance().timeInMillis,
    var name: String,
    var description: String,
    var priority: HabitPriority,
    var type: HabitType,
    var executionQuantity: Int,
    var frequency: Int,
    var color: Float,
) {
    fun toHabitDto(): HabitDTO {
        return HabitDTO(
            id = id.toString(),
            editDate = editDate,
            name = name,
            description = description,
            priority = priority.value,
            type = type.value,
            executionQuantity = executionQuantity,
            frequency = frequency,
            color = color.toInt()
        )
    }

    override fun toString(): String {
        return "|$name - $type - $id|"
    }
}

@Parcelize
enum class HabitType(val value: Int) : Parcelable {
    GOOD(0),
    BAD(1);

    companion object {
        fun getResourceIdByType(habitType: HabitType): Int {
            return when (habitType) {
                GOOD -> R.string.good_habit
                BAD -> R.string.bad_habit
            }
        }

        fun getHabitTypeByValue(value: Int): HabitType {
            return when (value) {
                0 -> GOOD
                else -> BAD
            }
        }
    }
}

enum class HabitPriority(val value: Int) {
    HIGH(0),
    MEDIUM(1),
    LOW(2);

    companion object {
        fun getResourceIdByPriority(priority: HabitPriority): Int {
            return when (priority) {
                HIGH -> R.string.high
                MEDIUM -> R.string.medium
                LOW -> R.string.low
            }
        }

        fun getHabitPriorityByValue(value: Int): HabitPriority {
            return when (value) {
                0 -> HIGH
                1 -> MEDIUM
                else -> LOW
            }
        }
    }
}