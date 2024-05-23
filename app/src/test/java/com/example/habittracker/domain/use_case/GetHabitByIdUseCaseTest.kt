package com.example.habittracker.domain.use_case

import com.example.domain.model.Habit
import com.example.domain.model.HabitPriority
import com.example.domain.model.HabitType
import com.example.domain.repository.HabitRepository
import com.example.domain.use_case.GetHabitByIdUseCase
import com.example.habittracker.utils.MainCoroutineRule
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class GetHabitByIdUseCaseTest {
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var getHabitByIdUseCase: GetHabitByIdUseCase
    private val fakeHabitRepo: HabitRepository = mock()

    @Before
    fun setUp() {
        getHabitByIdUseCase = GetHabitByIdUseCase(fakeHabitRepo)
    }

    @Test
    fun `GetHabitByIdUseCase returning habit`() = runTest {

        //Arrange
        val uuid = "uuid"
        val list = listOf(
            Habit(
                id = uuid,
                editDate = 123124123,
                name = "TestName1",
                description = "TestDescription1",
                type = HabitType.GOOD,
                priority = HabitPriority.LOW,
                doneDates = listOf(),
                frequency = 0,
                executionQuantity = 0,
                color = 0f
            )
        )

        whenever(fakeHabitRepo.getHabitById(uuid)).thenReturn(list.find {
            it.id == uuid
        })


        // Act
        val habit = getHabitByIdUseCase.invoke(uuid)


        // Assert
        assert(uuid == habit.id)
    }

    @Test
    fun `GetHabitByIdUseCase not returning habit`() = runTest {

        // Arrange
        val uuid = "uuid"
        val list = listOf(
            Habit(
                id = uuid,
                editDate = 123124123,
                name = "TestName1",
                description = "TestDescription1",
                type = HabitType.GOOD,
                priority = HabitPriority.LOW,
                doneDates = listOf(),
                frequency = 0,
                executionQuantity = 0,
                color = 0f
            )
        )

        whenever(fakeHabitRepo.getHabitById(uuid)).thenReturn(list.find {
            it.id == "Wrong id"
        })


        // Act
        val habit = getHabitByIdUseCase.invoke(uuid)

        // Assert
        TestCase.assertNull(habit)
    }
}