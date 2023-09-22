package com.comunidadedevspace.joaovazstudio.data

import androidx.room.Embedded
import androidx.room.Relation

data class TrainWithTasksData(
    @Embedded val train: Train,
    @Relation(
        parentColumn = "id",
        entityColumn = "trainId"
    )
    val tasks: List<Task>
)