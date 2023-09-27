package com.comunidadedevspace.joaovazstudio.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Exercise::class, User::class, Train::class], version = 6, exportSchema = false)
@TypeConverters(ExerciseIdListConverter::class)
abstract class AppDataBase: RoomDatabase() {
    abstract fun taskDao(): ExerciseDao
    abstract fun trainDao(): TrainDao
    abstract fun userDao(): UserDao
}