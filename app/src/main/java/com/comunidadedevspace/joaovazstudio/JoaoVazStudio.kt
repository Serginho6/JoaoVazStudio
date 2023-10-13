package com.comunidadedevspace.joaovazstudio

import android.app.Application
import androidx.room.Room
import com.comunidadedevspace.joaovazstudio.data.database.MIGRATION_12_13
import com.comunidadedevspace.joaovazstudio.data.local.AppDataBase

class JoaoVazStudio: Application() {

    private lateinit var dataBase : AppDataBase

    override fun onCreate() {
        super.onCreate()

        dataBase = Room.databaseBuilder(applicationContext, AppDataBase::class.java, "joao-vaz-studio-database")
            .addMigrations(MIGRATION_12_13)
            .build()
    }

    fun getAppDataBase(): AppDataBase {
        return dataBase
    }
}