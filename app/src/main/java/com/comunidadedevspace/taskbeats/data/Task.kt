package com.comunidadedevspace.taskbeats.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    var image: ByteArray?,
    var isSelected: Boolean,
): Serializable