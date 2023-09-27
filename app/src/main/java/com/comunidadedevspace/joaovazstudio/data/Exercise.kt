package com.comunidadedevspace.joaovazstudio.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "exercise")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Long,
    val trainId: Int,
    val title: String,
    val description: String,
    var youtubeVideoId: String?,
    var isSelected: Boolean,
): Serializable