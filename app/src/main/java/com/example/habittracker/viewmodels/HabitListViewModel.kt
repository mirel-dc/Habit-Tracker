package com.example.habittracker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habittracker.data.models.Habit
import com.example.habittracker.data.models.HabitType
import com.example.habittracker.domain.HabitList

class HabitListViewModel : ViewModel() {

    private val _habitsLiveData: MutableLiveData<List<Habit>> = MutableLiveData()

    val habitsLiveData: LiveData<List<Habit>> = _habitsLiveData

    init {
        importHabits()
    }

    fun importHabits() {
        _habitsLiveData.value = HabitList.getHabits()
    }

    fun habitsByType(habitType: HabitType): List<Habit> {
        return _habitsLiveData.value?.filter { it.type == habitType } ?: listOf()
    }


    val habitsByType = mutableMapOf<HabitType, MutableLiveData<List<Habit>>>(
        HabitType.GOOD to MutableLiveData(listOf()),
        HabitType.BAD to MutableLiveData(listOf())
    )
//
//    fun initObserver(habitType: HabitType): MediatorLiveData<List<Habit>> {
//        getHabits()
//        return MediatorLiveData<List<Habit>>()
//    }

//    private fun getHabits(newHabits: List<Habit>?, habitType: HabitType):List<Habit>{
//        val filtered = newHabits?.filter { it.type == habitType }
//
//        return filtered?.sortedWith(comparator) ?: listOf()
//    }


}
