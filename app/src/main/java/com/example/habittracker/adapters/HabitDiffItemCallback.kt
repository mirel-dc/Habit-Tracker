package com.example.habittracker.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.habittracker.data.models.Habit

class HabitDiffItemCallback() : DiffUtil.ItemCallback<Habit>() {
    override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
        return oldItem == newItem
    }

}