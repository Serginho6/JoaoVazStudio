package com.comunidadedevspace.joaovazstudio.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "exercise", foreignKeys = [
    ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = Train::class, parentColumns = ["id"], childColumns = ["trainId"], onDelete = ForeignKey.CASCADE)
])
data class Exercise(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    val userId: String,
    val trainId: String,
    val title: String,
    val description: String,
    var youtubeVideoId: String?,
    var isSelected: Boolean,
) : Serializable