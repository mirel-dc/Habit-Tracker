package com.example.habittracker.data.repository

import com.example.habittracker.data.local.db.HabitDB
import com.example.habittracker.data.local.entity.HabitEntity
import com.example.habittracker.data.remote.RetrofitInstance
import com.example.habittracker.data.remote.dto.UUIDDto
import com.example.habittracker.data.remote.utils.retryIO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.UUID

private const val TAG = "HabitRepository"

class HabitRepository(
    private val habitDB: HabitDB
) {
    suspend fun importHabitsFromApi() {
        withContext(Dispatchers.IO) {
            //Попытка обновить данные с сервера
            val remoteHabits = retryIO { RetrofitInstance.service.getHabitsList() }
            if (remoteHabits.isSuccessful) {
                withContext(Dispatchers.IO) {
                    habitDB.clearAllTables()
                    habitDB.getDao()
                        .importHabits(remoteHabits.body()!!.map { it.toHabitEntity() })
                }
            }
        }
    }

    fun getAllHabits() = habitDB.getDao().getAllHabits()

    suspend fun insertHabit(habitEntity: HabitEntity) = withContext(Dispatchers.IO) {
        //При создании нового - id должен быть null
        val habitUid = retryIO {
            RetrofitInstance.service.putHabit(habitEntity.toHabitDto().copy(id = null))
        }
        if (habitUid.isSuccessful) {
            habitDB.getDao().insert(habitEntity)
        }
        importHabitsFromApi()
    }

    suspend fun updateHabit(habitEntity: HabitEntity) = withContext(Dispatchers.IO) {
        habitEntity.editDate = Calendar.getInstance().timeInMillis
        val habitUid = retryIO {
            RetrofitInstance.service.putHabit(habitEntity.toHabitDto())
        }
        if (habitUid.isSuccessful) {
            habitDB.getDao().update(habitEntity)
        }
    }

    suspend fun deleteHabit(habitEntity: HabitEntity) = withContext(Dispatchers.IO) {
        val habitUid = retryIO {
            RetrofitInstance.service.deleteHabit(
                UUIDDto.UUIDtoUUIDDto(habitEntity.id)
            )
        }
        if (habitUid.isSuccessful) {
            habitDB.getDao().delete(habitEntity)
        }
    }

    suspend fun getHabitById(uuid: UUID): HabitEntity {
        return withContext(Dispatchers.IO) {
            habitDB.getDao().getHabitById(uuid)
        }
    }
}

