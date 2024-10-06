package com.example.habittracker.domain.use_case

import com.example.domain.model.Habit
import com.example.domain.model.HabitPriority
import com.example.domain.model.HabitType
import com.example.domain.use_case.FilterAndSearchHabitsUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class FilterAndSearchHabitsUseCaseTest {

    private lateinit var filterAndSearchHabitsUseCase: FilterAndSearchHabitsUseCase

    @Before
    fun setUp() {
        filterAndSearchHabitsUseCase = FilterAndSearchHabitsUseCase()
    }

    private val goodHabit1 = Habit(
        id = "Good1", type = HabitType.GOOD, name = "a",
        doneDates = listOf(), color = 0f, description = "",
        executionQuantity = 0, frequency = 0, editDate = 0, priority = HabitPriority.LOW
    )
    private val goodHabit2 = Habit(
        id = "Good2", type = HabitType.GOOD, name = "b",
        doneDates = listOf(), color = 0f, description = "",
        executionQuantity = 0, frequency = 0, editDate = 0, priority = HabitPriority.LOW
    )
    private val goodHabit3 = Habit(
        id = "Good3", type = HabitType.GOOD, name = "c",
        doneDates = listOf(), color = 0f, description = "",
        executionQuantity = 0, frequency = 0, editDate = 0, priority = HabitPriority.LOW
    )
    private val badHabit1 = Habit(
        id = "Bad1", type = HabitType.BAD, name = "d",
        doneDates = listOf(), color = 0f, description = "",
        executionQuantity = 0, frequency = 0, editDate = 0, priority = HabitPriority.LOW
    )
    private val badHabit2 = Habit(
        id = "Bad2", type = HabitType.BAD, name = "f",
        doneDates = listOf(), color = 0f, description = "",
        executionQuantity = 0, frequency = 0, editDate = 0, priority = HabitPriority.LOW
    )

    @Test
    fun `filterAndSearch should return habits of specified type - good type`() {

        // Arrange
        val habits = listOf(
            goodHabit1, badHabit1, goodHabit2, goodHabit3, badHabit2
        )


        // Act
        val actual = filterAndSearchHabitsUseCase.filterAndSearch(
            habits,
            HabitType.GOOD,
            "",
            true
        )


        // Assert
        val expected = listOf(
            goodHabit1, goodHabit2, goodHabit3
        )
        assertEquals(
            expected, actual
        )
    }

    @Test
    fun `filterAndSearch should return habits of specified type - bad type`() {

        // Arrange
        val habits = listOf(
            goodHabit1, badHabit1, goodHabit2, goodHabit3, badHabit2
        )


        // Act
        val actual = filterAndSearchHabitsUseCase.filterAndSearch(
            habits,
            HabitType.BAD,
            "",
            true
        )


        // Assert
        val expected = listOf(
            badHabit1, badHabit2
        )
        assertEquals(
            expected, actual
        )
    }

    @Test
    fun `filterAndSearch should return habits matching search string and good type`() {

        // Arrange
        val habits = listOf(
            goodHabit1, badHabit1, goodHabit2, goodHabit3, badHabit2
        )


        // Act
        val actual = filterAndSearchHabitsUseCase.filterAndSearch(
            habits,
            HabitType.GOOD,
            "b",
            true
        )


        // Assert
        val expected = listOf(
            goodHabit2
        )
        assertEquals(
            expected, actual
        )
    }

    @Test
    fun `filterAndSearch should return empty list Act no habits match the criteria`() {

        // Arrange
        val habits = listOf(
            goodHabit1, badHabit1, goodHabit2, goodHabit3, badHabit2
        )


        // Act
        val actual = filterAndSearchHabitsUseCase.filterAndSearch(
            habits,
            HabitType.GOOD,
            "xyz",
            true
        )


        // Assert
        val expected = emptyList<Habit>()

        assertEquals(
            expected, actual
        )
    }


    @Test
    fun `filterAndSearch should return empty list Act there are no habits`() {
        // Arrange
        val habits = emptyList<Habit>()


        // Act
        val actual = filterAndSearchHabitsUseCase.filterAndSearch(
            habits,
            HabitType.GOOD,
            "test",
            true
        )


        // Assert
        val expected = emptyList<Habit>()
        assertEquals(expected, actual)
    }
}