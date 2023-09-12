package com.comunidadedevspace.joaovazstudio

import android.app.Application
import androidx.room.Room
import com.comunidadedevspace.joaovazstudio.data.AppDataBase
import com.google.firebase.FirebaseApp

class JoaoVazStudio: Application() {

    lateinit var dataBase : AppDataBase

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        dataBase = Room.databaseBuilder(applicationContext, AppDataBase::class.java, "joao-vaz-studio-database")
            .addMigrations(AppDataBase.MIGRATION_1_2, AppDataBase.MIGRATION_2_3, AppDataBase.MIGRATION_3_4, AppDataBase.MIGRATION_4_5)
            .build()
    }

    fun getAppDataBase(): AppDataBase {
        return dataBase
    }
}