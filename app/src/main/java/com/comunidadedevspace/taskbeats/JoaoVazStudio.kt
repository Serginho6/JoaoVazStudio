package com.comunidadedevspace.taskbeats

import android.app.Application
import androidx.room.Room
import com.comunidadedevspace.taskbeats.data.AppDataBase

class JoaoVazStudio: Application() {

    lateinit var dataBase : AppDataBase

    override fun onCreate() {
        super.onCreate()

        dataBase = Room.databaseBuilder(applicationContext, AppDataBase::class.java, "joaovazstudio-database")
            .addMigrations(AppDataBase.MIGRATION_1_2, AppDataBase.MIGRATION_2_3, AppDataBase.MIGRATION_3_4)
            .build()
    }

    fun getAppDataBase(): AppDataBase {
        return dataBase
    }
}