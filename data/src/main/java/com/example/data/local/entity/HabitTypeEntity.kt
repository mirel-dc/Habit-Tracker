package com.example.data.local.entity

import android.os.Parcelable

@Parcelize
enum class HabitTypeEntity(val value: Int) : Parcelable {
    GOOD(0),
    BAD(1);

    companion object {
        fun getResourceIdByType(habitType: HabitType): Int {
            return when (habitType) {
                GOOD -> R.string.good_habit
                BAD -> R.string.bad_habit
            }
        }

        fun getResIdByValue(value: Int): Int {
            return when (value) {
                0 -> R.string.good_habit
                else -> R.string.bad_habit
            }
        }
    }
}
