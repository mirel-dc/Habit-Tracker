package com.example.data.repository

import com.example.data.local.db.HabitDao
import com.example.data.remote.HabitApi
import com.example.data.remote.dto.Mapper
import com.example.data.remote.dto.UUIDDto
import com.example.data.remote.utils.retryIO
import com.example.domain.model.Habit
import com.example.domain.repository.HabitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.UUID

private const val TAG = "HabitRepository"

class HabitRepositoryImpl(
    private val api: HabitApi,
    private val dao: HabitDao
) : HabitRepository {

    override fun getHabits(): Flow<List<Habit>> = dao.getAllHabits().map { habits ->
        habits.map { it.toHabit() }
    }

    override suspend fun importHabits() {
        withContext(Dispatchers.IO) {
            //Попытка обновить данные с сервера
            val remoteHabits = retryIO { api.getHabitsList() }
            if (remoteHabits.isSuccessful) {
                dao.nukeHabits()
                dao.importHabits(remoteHabits.body()!!.map { it.toHabitEntity() })
            }
        }
    }

    override suspend fun insertHabit(habit: Habit) {
        withContext(Dispatchers.IO) {
            val habitDto = Mapper.fromHabitToHabitDto(habit)

            //При создании нового - id должен быть null
            val habitUid = retryIO {
                api.putHabit(habitDto.copy(id = null))
            }
            if (habitUid.isSuccessful) {
                dao.insert(habitDto.copy(id = habitUid.body()?.uid.toString()).toHabitEntity())
            }
            //TODO fix ids
            //importHabitsFromApi()
        }
    }

    override suspend fun updateHabit(habit: Habit) =
        withContext(Dispatchers.IO) {
            habit.editDate = Calendar.getInstance().timeInMillis
            val habitDto = Mapper.fromHabitToHabitDto(habit)

            val habitUid = retryIO {
                api.putHabit(habitDto)
            }
            if (habitUid.isSuccessful) {
                dao.update(habitDto.toHabitEntity())
            }
        }

    override suspend fun getHabitById(uuid: String): Habit {
        return withContext(Dispatchers.IO) {
            dao.getHabitById(UUID.fromString(uuid)).toHabit()
        }
    }

    override suspend fun deleteHabit(habit: Habit) =
        withContext(Dispatchers.IO) {
            val habitDto = Mapper.fromHabitToHabitDto(habit)

            val habitUid = retryIO {
                api.deleteHabit(UUIDDto.stringToUUIDDto(habit.id))
            }
            if (habitUid.isSuccessful) {
                dao.delete(habitDto.toHabitEntity())
            }
        }
}

