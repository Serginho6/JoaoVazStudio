package com.devserginho.joaovazstudio.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "train")
data class Train(
    @PrimaryKey
    val nome: String,
    val desc: String,
) : Serializable {
    constructor() : this("", "")
}