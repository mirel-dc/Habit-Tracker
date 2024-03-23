package com.example.habittracker.data.models

import android.os.Parcelable
import com.example.habittracker.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class Habit(
    var name: String,
    var description: String?,
    var priority: Int,
    var type: HabitType,
    var executionQuantity: Int,
    var frequency: Int,
    var color: Float
) : Parcelable

@Parcelize
enum class HabitType(val resId  : Int) : Parcelable {
    GOOD(R.string.good_habit),
    BAD(R.string.bad_habit);
}