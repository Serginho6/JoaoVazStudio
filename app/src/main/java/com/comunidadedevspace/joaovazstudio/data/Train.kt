package com.comunidadedevspace.joaovazstudio.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Train(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Long,
    val trainTitle: String,
)
