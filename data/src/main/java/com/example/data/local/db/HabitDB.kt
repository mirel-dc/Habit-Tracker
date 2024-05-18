package com.example.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.local.entity.HabitEntity

@Database(
    entities = [HabitEntity::class],
    version = 3,
)
//TODO убрал хабитконвертер
//@TypeConverters(UUIDConverter::class)
abstract class HabitDB : RoomDatabase() {
    abstract val dao: HabitDao


//    abstract fun getDao(): HabitDao
//
//    //нужен сингинстанс, потому что иначе при добавлении из фрагмента создания, не отображается
//    //хотя и появляется запись в бд
//    //как я понял, пофикситься с DI
//    companion object {
//        @Volatile
//        private var instance: HabitDB? = null
//        private val LOCK = Any()
//
//        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
//            instance ?: createDatabase(context).also { instance = it }
//        }
//
//        private fun createDatabase(context: Context) =
//            Room.databaseBuilder(
//                context.applicationContext,
//                HabitDB::class.java,
//                "Habit.db"
//            ).build()
//    }
}