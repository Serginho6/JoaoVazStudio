package com.comunidadedevspace.joaovazstudio.presentation

import com.comunidadedevspace.joaovazstudio.data.Exercise
import com.comunidadedevspace.joaovazstudio.data.Train
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