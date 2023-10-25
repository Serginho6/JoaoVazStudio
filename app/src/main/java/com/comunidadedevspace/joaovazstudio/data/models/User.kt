package com.comunidadedevspace.joaovazstudio.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val name: String,
    val email: String,
    var password: String,
    val phone: String,
    val gender: String,
    val height: String,
    val weight: String
) : Serializable