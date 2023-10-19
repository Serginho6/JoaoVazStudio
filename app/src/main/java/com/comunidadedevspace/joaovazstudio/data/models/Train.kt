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
    val trainTitle: String,
    val trainDesc: String,
    val trainId: String,
) : Serializable {
    constructor() : this("", "", "")
}