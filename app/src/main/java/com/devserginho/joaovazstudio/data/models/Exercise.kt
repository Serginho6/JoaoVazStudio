package com.devserginho.joaovazstudio.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "exercise")
data class Exercise(
    @PrimaryKey
    val nome: String,
    val desc: String,
    val youtube: String?,
    val checked: Boolean,
) : Serializable {
    constructor() : this("", "", "", false)
}