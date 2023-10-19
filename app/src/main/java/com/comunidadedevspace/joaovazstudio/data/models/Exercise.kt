package com.comunidadedevspace.joaovazstudio.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "exercise",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Train::class, parentColumns = ["id"], childColumns = ["trainId"], onDelete = ForeignKey.CASCADE)
    ]
)
data class Exercise(
    @PrimaryKey
    val title: String,
    val desc: String,
    var youtube: String?,
) : Serializable {
    var isSelected: Boolean = false

    constructor() : this("", "", "")
}