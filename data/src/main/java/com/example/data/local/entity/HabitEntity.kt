package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.remote.dto.HabitDTO
import com.example.domain.model.Habit
import com.example.domain.model.HabitPriority
import com.example.domain.model.HabitType
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

    fun toHabit(): Habit {
        return Habit(
            id = id.toString(),
            editDate = editDate,
            name = name,
            description = description,
            priority = priority,
            type = type,
            executionQuantity = executionQuantity,
            frequency = frequency,
            color = color,
        )
    }

    override fun toString(): String {
        return "|$name - $type - $id|"
    }
}

//enum class HabitPriorityEntity(val value: Int) {
//    HIGH(0),
//    MEDIUM(1),
//    LOW(2);
//
//   companion object {
//        fun getResourceIdByValue(priority: HabitPriorityEntity): Int {
//            return when (priority) {
//                HIGH -> R.string.high
//                MEDIUM -> R.string.medium
//                LOW -> R.string.low
//            }
//        }
//
//        fun getStringIdByValue(value: Int): Int {
//            return when (value) {
//                0 -> R.string.high
//                1 -> R.string.medium
//                else -> R.string.low
//            }
//        }
//    }
//}

//@Parcelize
//enum class HabitTypeEntity(val value: Int) : Parcelable {
//    GOOD(0),
//    BAD(1);
//
//    companion object {
//        fun getResourceIdByType(habitType: HabitType): Int {
//            return when (habitType) {
//                GOOD -> R.string.good_habit
//                BAD -> R.string.bad_habit
//            }
//        }
//
//        fun getResIdByValue(value: Int): Int {
//            return when (value) {
//                0 -> R.string.good_habit
//                else -> R.string.bad_habit
//            }
//        }
//    }
//}


