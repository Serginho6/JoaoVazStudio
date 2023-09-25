package com.comunidadedevspace.joaovazstudio.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Train(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Long,
    val trainTitle: String,
    val trainDescription: String,
) :Serializable