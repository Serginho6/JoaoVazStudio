package com.comunidadedevspace.joaovazstudio.data.database

import com.comunidadedevspace.joaovazstudio.data.local.Exercise
import com.comunidadedevspace.joaovazstudio.data.local.Train
import java.io.Serializable

enum class ActionType {
    DELETE,
    UPDATE,
    CREATE,
}

data class TaskAction(
    val exercise: Exercise?,
    val taskActionType: String
) : Serializable

data class TrainAction(
    val train: Train?,
    val trainActionType: String
) : Serializable