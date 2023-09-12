package com.comunidadedevspace.joaovazstudio.presentation

import com.comunidadedevspace.joaovazstudio.data.Task
import java.io.Serializable

enum class ActionType {
    DELETE,
    UPDATE,
    CREATE,
}

data class TaskAction(
    val task: Task?,
    val actionType: String
) : Serializable