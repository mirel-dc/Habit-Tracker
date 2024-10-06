package com.example.data.repository

import com.example.data.local.db.HabitDao
import com.example.data.remote.HabitApi
import com.example.data.remote.dto.Mapper
import com.example.data.remote.dto.UUIDDto
import com.example.data.remote.utils.retryIO
import com.example.domain.model.Habit
import com.example.domain.model.HabitCountState
import com.example.domain.model.HabitType
import com.example.domain.repository.HabitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.UUID

private const val TAG = "HabitRepository"

class HabitRepositoryImpl(
    private val api: HabitApi,
    private val dao: HabitDao
) : HabitRepository {

    override fun completeHabit(habit: Habit): Flow<HabitCountState> = flow {
        val maxCount = habit.frequency
        val currentCount = habit.executionQuantity + 1

        //Increase count by 1
        updateHabit(habit.copy(executionQuantity = habit.executionQuantity + 1))

        when (habit.type) {
            HabitType.GOOD -> {
                if (currentCount < maxCount) {
                    emit(HabitCountState.KEEP_DOING)
                } else {
                    emit(HabitCountState.URE_BREATHTAKING)
                }
            }

            HabitType.BAD -> {
                if (currentCount < maxCount) {
                    emit(HabitCountState.STOP_IT)
                } else {
                    emit(HabitCountState.NO_MORE)
                }
            }
        }

        //Пока просто закидываю done-date на сервер, не придумал как использовать их лучше всего
        //Mapper установит текущую дату (в милисек) и uid привычки
        api.doneHabit(Mapper.fromHabitToDoneHabitDto(habit))
    }


    override fun getHabits(): Flow<List<Habit>> = dao.getAllHabits().map { habits ->
        habits.map { habit ->
            habit.toHabit()
        }
    }

    override suspend fun importHabits() {
        withContext(Dispatchers.IO) {
            //Попытка обновить данные с сервера
            val remoteHabits = retryIO { api.getHabitsList() }
            if (remoteHabits.isSuccessful) {
                dao.nukeHabits()
                remoteHabits.body()?.let { list ->
                    dao.importHabits(
                        list.map { item ->
                            item.toHabitEntity()
                        })
                }
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

