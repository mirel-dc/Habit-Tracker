package com.example.habittracker.viewmodels

import android.text.TextUtils
import androidx.lifecycle.ViewModel
import com.example.habittracker.data.models.Habit
import com.example.habittracker.domain.HabitList

class CreateHabitViewModel : ViewModel() {

    val priorities = arrayOf(1, 2, 3, 4, 5)

    fun createHabit(habit: Habit) {
        HabitList.createHabit(
            Habit(
                name = habit.name,
                description = habit.description,
                type = habit.type,
                color = habit.color,
                priority = habit.priority,
                executionQuantity = habit.executionQuantity,
                frequency = habit.frequency
            )
        )
    }

    //wasHabitName could be changed with UID
    fun updateHabit(wasHabitName: String, habit: Habit) {
        HabitList.updateHabit(
            wasHabitName, Habit(
                name = habit.name,
                description = habit.description,
                type = habit.type,
                color = habit.color,
                priority = habit.priority,
                executionQuantity = habit.executionQuantity,
                frequency = habit.frequency
            )
        )
    }

    //TODO VALIDATION
    private fun validateData(habit: Habit): Boolean {
       return !TextUtils.isEmpty(habit.name)
                && habit.frequency != null
                && habit.executionQuantity != null
    }

}