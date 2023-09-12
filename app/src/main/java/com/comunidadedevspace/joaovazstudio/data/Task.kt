package com.comunidadedevspace.joaovazstudio.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Long,
    val title: String,
    val description: String,
    var image: ByteArray?,
    var isSelected: Boolean,
): Serializable