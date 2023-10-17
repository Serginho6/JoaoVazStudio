package com.comunidadedevspace.joaovazstudio.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.comunidadedevspace.joaovazstudio.data.database.ExerciseIdListConverter
import java.io.Serializable

@Entity(
    tableName = "train",
    foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userUid"], onDelete = ForeignKey.CASCADE)]
)
data class Train(
    @PrimaryKey
    var id: String = "",
    val userUid: String,
    val trainTitle: String,
    val trainDescription: String,
    @TypeConverters(ExerciseIdListConverter::class)
    val exerciseIds: List<Int> = emptyList()
) : Serializable {
    constructor() : this("", "", "", "", emptyList())

}