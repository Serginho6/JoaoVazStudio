package com.comunidadedevspace.joaovazstudio.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "train",
    foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userUid"], onDelete = ForeignKey.CASCADE)]
)
data class Train(
    @PrimaryKey
    var trainId: String,
    val userUid: String,
    val trainTitle: String,
    val trainDescription: String,
) : Serializable {
    constructor() : this("", "", "", "")
}