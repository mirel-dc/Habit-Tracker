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
    var description: String?,
    var priority: Int,
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
            description = description ?: "",
            priority = priority,
            type = HabitType.getApiId(type.resId),
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
enum class HabitType(val resId: Int) : Parcelable {
    GOOD(R.string.good_habit),
    BAD(R.string.bad_habit);

    companion object {
        fun getByResId(resId: Int): HabitType {
            return when (resId) {
                GOOD.resId -> GOOD
                else -> BAD
            }
        }

        fun getApiId(resId: Int): Int {
            return when (resId) {
                GOOD.resId -> 0
                else -> 1
            }
        }

        fun getFromApiId(apiId: Int): HabitType {
            return when (apiId) {
                0 -> GOOD
                else -> BAD
            }
        }
    }
}