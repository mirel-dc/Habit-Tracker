package com.example.habittracker.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.habittracker.data.local.entity.HabitEntity

class HabitDiffItemCallback() : DiffUtil.ItemCallback<HabitEntity>() {
    override fun areItemsTheSame(oldItem: HabitEntity, newItem: HabitEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HabitEntity, newItem: HabitEntity): Boolean {
        return oldItem == newItem
    }

}