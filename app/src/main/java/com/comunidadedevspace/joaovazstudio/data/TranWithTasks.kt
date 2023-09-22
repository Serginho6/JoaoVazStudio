package com.comunidadedevspace.joaovazstudio.data

import androidx.room.Entity

@Entity(primaryKeys = ["trainId", "taskId"])
data class TrainWithTasks(
    val trainId: Int,
    val taskId: Int
)
