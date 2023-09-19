package com.comunidadedevspace.joaovazstudio.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val email: String,
    val password: String,
    val phone: String,
    val gender: String,
    val height: String,
    val weight: String
)