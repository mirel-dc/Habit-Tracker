package com.example.habittracker.data.models

import android.os.Parcelable
import com.example.habittracker.R
import kotlinx.parcelize.Parcelize
import java.util.Calendar
import java.util.UUID

data class Habit(
    var name: String,
    var description: String?,
    var priority: Int,
    var type: HabitType,
    var executionQuantity: Int,
    var frequency: Int,
    var color: Float,
    val id: UUID = UUID.randomUUID(),
    val editDate: Long = Calendar.getInstance().timeInMillis
) {
    override fun toString(): String {
        return "|$name - $type - $id|"
    }
}

@Parcelize
enum class HabitType(val resId: Int) : Parcelable {
    GOOD(R.string.good_habit),
    BAD(R.string.bad_habit);
}