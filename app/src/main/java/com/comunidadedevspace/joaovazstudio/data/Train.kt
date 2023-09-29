package com.comunidadedevspace.joaovazstudio.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable

@Entity(tableName = "train", foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE)])
data class Train(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Long,
    val trainTitle: String,
    val trainDescription: String,
    @TypeConverters(ExerciseIdListConverter::class)
    val exerciseIds: List<Int> = emptyList()
) : Serializable