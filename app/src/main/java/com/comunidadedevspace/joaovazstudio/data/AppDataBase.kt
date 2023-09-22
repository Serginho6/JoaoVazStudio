package com.comunidadedevspace.joaovazstudio.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Task::class, User::class], version = 6, exportSchema = false)
abstract class AppDataBase: RoomDatabase() {
    abstract fun taskDao(): TaskDao

//    abstract fun trainDao(): TrainDao

    abstract fun userDao(): UserDao
}