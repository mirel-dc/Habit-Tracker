package com.example.habittracker.viewmodels

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.R
import com.example.habittracker.data.models.Habit
import com.example.habittracker.repository.HabitRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.UUID

private const val TAG = "CreateHabitViewModel"

class CreateHabitViewModel(
    private val habitRepository: HabitRepository
) : ViewModel() {
    var currentHabit = MutableLiveData<Habit?>()
        private set

    var priorities = arrayOf(1, 2, 3, 4, 5)
        private set

    private val _nameError = MutableLiveData<Int?>()
    val nameError: LiveData<Int?> = _nameError

    private val _frequencyError = MutableLiveData<Int?>()
    val frequencyError: LiveData<Int?> = _frequencyError

    private val _quantityError = MutableLiveData<Int?>()
    val quantityError: LiveData<Int?> = _quantityError

    fun clearCurrentHabit() {
        currentHabit.value = null
    }

    fun createHabit() = viewModelScope.launch {
        Log.d(TAG, currentHabit.value.toString())
        currentHabit.value.let {
            if (it != null) {
                habitRepository.insertHabit(it)
            }
        }
    }

    fun updateHabit() = viewModelScope.launch {
        currentHabit.value.let {
            if (it != null) {
                habitRepository.updateHabit(it)
            }
        }
    }

    fun setCurrentHabitWithUUID(habitUUID: String?) = viewModelScope.launch {
        val habit = async { habitRepository.getHabitById(UUID.fromString(habitUUID)) }
        currentHabit.value = habit.await()
        initValidationErrors()
    }

    fun setCurrentHabitWithObject(habit: Habit) {
        currentHabit.value = habit
    }

    fun initValidationErrors() {
        if (currentHabit.value == null) {
            _nameError.value = R.string.cannot_be_empty
            _quantityError.value = R.string.cannot_be_empty
            _frequencyError.value = R.string.cannot_be_empty
        } else {
            _nameError.value = null
            _quantityError.value = null
            _frequencyError.value = null
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
}