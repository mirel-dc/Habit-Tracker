package com.example.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.domain.model.HabitType
import com.example.presentation.fragments.HabitsListFragment

class TypeFragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = HabitType.entries.size

    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> HabitsListFragment.newInstance(HabitType.GOOD)
        else -> HabitsListFragment.newInstance(HabitType.BAD)
    }
}