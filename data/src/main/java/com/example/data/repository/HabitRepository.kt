package com.example.data.repository

import com.example.data.local.db.HabitDao
import com.example.data.local.entity.HabitEntity
import com.example.data.remote.RetrofitInstance
import com.example.domain.repository.HabitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.UUID

private const val TAG = "HabitRepository"

class HabitRepositoryImpl(
    private val api: RetrofitInstance,
    private val dao: HabitDao
) : HabitRepository {
    suspend fun importHabitsFromApi() {
        withContext(Dispatchers.IO) {
            //Попытка обновить данные с сервера
            val remoteHabits =
                com.example.data.remote.utils.retryIO { RetrofitInstance.service.getHabitsList() }
            if (remoteHabits.isSuccessful) {
                withContext(Dispatchers.IO) {
                    //dao.clearAllTables()
                    dao.importHabits(remoteHabits.body()!!.map { it.toHabitEntity() })
                }
            }
        }
    }

    fun getAllHabits() = dao.getAllHabits()

    suspend fun insertHabit(habitEntity: HabitEntity) =
        withContext(Dispatchers.IO) {
            //При создании нового - id должен быть null
            val habitUid = com.example.data.remote.utils.retryIO {
                RetrofitInstance.service.putHabit(
                    habitEntity.toHabitDto().copy(id = null)
                )
            }
            if (habitUid.isSuccessful) {
                dao.insert(habitEntity)
            }
            //TODO fix ids
            importHabitsFromApi()
        }

    suspend fun updateHabit(habitEntity: HabitEntity) =
        withContext(Dispatchers.IO) {
            habitEntity.editDate = Calendar.getInstance().timeInMillis
            val habitUid = com.example.data.remote.utils.retryIO {
                RetrofitInstance.service.putHabit(habitEntity.toHabitDto())
            }
            if (habitUid.isSuccessful) {
                dao.update(habitEntity)
            }
        }

    suspend fun deleteHabit(habitEntity: HabitEntity) =
        withContext(Dispatchers.IO) {
            val habitUid = com.example.data.remote.utils.retryIO {
                RetrofitInstance.service.deleteHabit(
                    com.example.data.remote.dto.UUIDDto.UUIDtoUUIDDto(habitEntity.id)
                )
            }
            if (habitUid.isSuccessful) {
                dao.delete(habitEntity)
            }
        }

    suspend fun getHabitById(uuid: UUID): HabitEntity {
        return withContext(Dispatchers.IO) {
            dao.getHabitById(uuid)
        }
    }
}

