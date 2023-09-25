package com.comunidadedevspace.joaovazstudio.presentation

import com.comunidadedevspace.joaovazstudio.data.Task
import com.comunidadedevspace.joaovazstudio.data.Train
import java.io.Serializable

enum class ActionType {
    DELETE,
    UPDATE,
    CREATE,
}

data class TaskAction(
    val task: Task?,
    val taskActionType: String
) : Serializable

data class TrainAction(
    val train: Train?,
    val trainActionType: String
) : Serializable