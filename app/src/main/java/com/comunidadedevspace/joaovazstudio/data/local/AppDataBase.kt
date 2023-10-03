package com.comunidadedevspace.joaovazstudio.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.comunidadedevspace.joaovazstudio.data.database.ExerciseIdListConverter

@Database(entities = [Exercise::class, User::class, Train::class], version = 6, exportSchema = false)
@TypeConverters(ExerciseIdListConverter::class)
abstract class AppDataBase: RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun trainDao(): TrainDao
    abstract fun userDao(): UserDao
}