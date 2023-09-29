package com.comunidadedevspace.joaovazstudio

import android.app.Application
import androidx.room.Room
import com.comunidadedevspace.joaovazstudio.data.AppDataBase
import com.comunidadedevspace.joaovazstudio.data.MIGRATION_9_10
import com.google.firebase.FirebaseApp

class JoaoVazStudio: Application() {

    private lateinit var dataBase : AppDataBase

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        dataBase = Room.databaseBuilder(applicationContext, AppDataBase::class.java, "joao-vaz-studio-database")
            .addMigrations(MIGRATION_9_10)
            .build()
    }

    fun getAppDataBase(): AppDataBase {
        return dataBase
    }
}