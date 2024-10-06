package com.example.domain.use_case

class ValidateHabitUseCase {
    fun validateName(name: String): Boolean {
        return if (name.isEmpty()) {
            false
        } else {
            true
        }
    }

    fun validateDescription(description: String): Boolean {
        return if (description.isEmpty()) {
            false
        } else {
            true
        }
    }

    fun validateQuantity(quantity: String): Boolean {
        return if (quantity.isEmpty()) {
            false
        } else {
            true
        }
    }

    fun validateFrequency(frequency: String): Boolean {
        return if (frequency.isEmpty()) {
            false
        } else {
            true
        }
    }

    fun isValid(
        name: String,
        description: String,
        quantity: String,
        frequency: String
    ): Boolean {
        return (validateName(name) &&
                validateDescription(description) &&
                validateQuantity(quantity) &&
                validateFrequency(frequency))
    }
}