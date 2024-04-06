package com.example.habittracker.viewmodels

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habittracker.R
import com.example.habittracker.data.models.Habit
import com.example.habittracker.domain.HabitList

private const val TAG = "CreateHabitViewModel"

class CreateHabitViewModel : ViewModel() {

    var currentHabit: Habit? = null
    val priorities = arrayOf(1, 2, 3, 4, 5)

    private val _nameError = MutableLiveData<Int?>()
    val nameError: LiveData<Int?> = _nameError

    private val _frequencyError = MutableLiveData<Int?>()
    val frequencyError: LiveData<Int?> = _frequencyError

    private val _quantityError = MutableLiveData<Int?>()
    val quantityError: LiveData<Int?> = _quantityError

    fun initValidationErrors() {
        if (currentHabit == null) {
            _nameError.value = R.string.cannot_be_empty
            _quantityError.value = R.string.cannot_be_empty
            _frequencyError.value = R.string.cannot_be_empty
        }
    }

    fun validateName(enteredName: String): Boolean {
        return if (TextUtils.isEmpty(enteredName)) {
            _nameError.value = R.string.cannot_be_empty
            false
        } else {
            _nameError.value = null
            true
        }
    }

    fun validateQuantity(enteredQuantity: String): Boolean {
        return if (enteredQuantity == "") {
            _quantityError.value = R.string.cannot_be_empty
            false
        } else {
            _quantityError.value = null
            true
        }
    }

    fun validateFrequency(enteredFrequency: String): Boolean {
        return if (enteredFrequency == "") {
            _frequencyError.value = R.string.cannot_be_empty
            false
        } else if (enteredFrequency.toInt() > 7) {
            _frequencyError.value = R.string.cannot_be_more_then_7
            false
        } else {
            _frequencyError.value = null
            true
        }
    }

    fun createHabit() {
        currentHabit?.let {
            HabitList.createHabit(
                Habit(
                    name = it.name,
                    description = it.description,
                    type = it.type,
                    color = it.color,
                    priority = it.priority,
                    executionQuantity = it.executionQuantity,
                    frequency = it.frequency
                )
            )
        }
    }

    //wasHabitName could be changed with UID
    fun updateHabit() {
        HabitList.updateHabit(currentHabit!!)
        Log.d(TAG + "Update Habit", HabitList.getHabits().toString())
    }
}