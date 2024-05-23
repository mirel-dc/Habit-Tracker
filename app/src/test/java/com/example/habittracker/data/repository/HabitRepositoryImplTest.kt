package com.example.habittracker.data.repository

import com.example.data.local.db.HabitDao
import com.example.data.remote.HabitApi
import com.example.data.remote.dto.HabitDTO
import com.example.data.remote.dto.UUIDDto
import com.example.data.repository.HabitRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response
import java.util.UUID

@ExperimentalCoroutinesApi
class HabitRepositoryImplTest {

    private lateinit var habitRepository: HabitRepositoryImpl
    private val api: HabitApi = mock()
    private val dao: HabitDao = mock()

    @Before
    fun setUp() {
        habitRepository = HabitRepositoryImpl(api, dao)
    }


    //HabitCountState tests are in domain layer (CompleteHabitUseCaseTest)


    private val habitDto1 = HabitDTO(
        id = UUID.randomUUID().toString(), type = 0, name = "HabitTestDto1",
        doneDates = listOf(), color = 0, description = "",
        executionQuantity = 0, frequency = 0, editDate = 0, priority = 0
    )
    private val habitDto2 = HabitDTO(
        id = UUID.randomUUID().toString(), type = 1, name = "HabitTestDto2",
        doneDates = listOf(), color = 0, description = "",
        executionQuantity = 0, frequency = 0, editDate = 0, priority = 1
    )

    @Test
    fun `getHabits should return list of habits`() = runTest {
        // Arrange
        val habitEntities = listOf(
            habitDto1, habitDto2
        )
        whenever(dao.getAllHabits()).thenReturn(
            flowOf(
                habitEntities.map {
                    it.toHabitEntity()
                }
            )
        )


        // Act
        val result = habitRepository.getHabits()


        // Assert
        result.collect { habits ->
            assert(habits.size == habitEntities.size)
        }
    }

    @Test
    fun `importHabits should update local database with remote habits`() = runTest {
        // Arrange
        val habitEntities = listOf(
            habitDto1, habitDto2
        )
        val response = Response.success(habitEntities)
        whenever(api.getHabitsList()).thenReturn(response)


        // Act
        habitRepository.importHabits()


        // Assert
        verify(dao).nukeHabits()
        verify(dao).importHabits(any())
    }

    @Test
    fun `insertHabit should insert habit into local database`() = runTest {

        // Arrange
        val response = Response.success(UUIDDto(UUID.randomUUID()))
        whenever(api.putHabit(any())).thenReturn(response)


        // Act
        habitRepository.insertHabit(habitDto1.toHabit())


        // Assert
        verify(dao).insert(any())
    }

    @Test
    fun `updateHabit should update habit in local database`() = runTest {

        // Arrange
        whenever(api.putHabit(any())).thenReturn(Response.success(null))


        // Act
        habitRepository.updateHabit(habitDto1.toHabit())


        // Assert
        verify(dao).update(any())
    }

    @Test
    fun `getHabitById should return habit from local database`() = runTest {

        // Arrange
        whenever(dao.getHabitById(any())).thenReturn(habitDto1.toHabitEntity())


        // Act
        val result = habitDto1.id?.let { habitRepository.getHabitById(it) }


        // Assert
        assert(result == habitDto1.toHabit())
    }

    @Test
    fun `deleteHabit should delete habit from local database`() = runTest {

        // Arrange
        whenever(api.deleteHabit(any())).thenReturn(Response.success(null))

        // Act
        habitRepository.deleteHabit(habitDto1.toHabit())

        // Assert
        verify(dao).delete(any())
    }
}