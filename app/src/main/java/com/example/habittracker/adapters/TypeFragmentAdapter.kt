package com.example.habittracker.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.habittracker.data.models.HabitType
import com.example.habittracker.fragments.HabitsListFragment

class TypeFragmentAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = HabitType.entries.size

    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> HabitsListFragment.newInstance(HabitType.GOOD)
        else -> HabitsListFragment.newInstance(HabitType.BAD)
    }
}