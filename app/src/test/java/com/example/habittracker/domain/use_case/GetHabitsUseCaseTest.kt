package com.example.habittracker.domain.use_case

import com.example.domain.model.Habit
import com.example.domain.model.HabitPriority
import com.example.domain.model.HabitType
import com.example.domain.repository.HabitRepository
import com.example.domain.use_case.GetHabitsUseCase
import com.example.habittracker.utils.MainCoroutineRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class GetHabitsUseCaseTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var getHabitsUseCase: GetHabitsUseCase
    private val fakeHabitRepo: HabitRepository = mock()

    @Before
    fun setUp() {
        getHabitsUseCase = GetHabitsUseCase(fakeHabitRepo)
    }

    @Test
    fun `invoke should return list of habits`() = runTest {

        // Arrange
        val habits = listOf(
            Habit(
                id = "1",
                editDate = 0,
                name = "TestName1",
                description = "TestDescription1",
                type = HabitType.GOOD,
                priority = HabitPriority.LOW,
                doneDates = listOf(),
                frequency = 0,
                executionQuantity = 0,
                color = 0f
            ),
            Habit(
                id = "2",
                editDate = 0,
                name = "TestName2",
                description = "TestDescription1",
                type = HabitType.GOOD,
                priority = HabitPriority.LOW,
                doneDates = listOf(),
                frequency = 0,
                executionQuantity = 0,
                color = 0f
            ),
        )
        whenever(fakeHabitRepo.getHabits()).thenReturn(flow {
            emit(habits)
        })

        // Act
        val result = getHabitsUseCase()


        // Assert
        result.collect { list ->
            assertEquals(habits, list)
        }
        verify(fakeHabitRepo).getHabits()
    }
}
