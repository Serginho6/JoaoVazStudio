package com.comunidadedevspace.joaovazstudio.presentation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.comunidadedevspace.joaovazstudio.JoaoVazStudio
import com.comunidadedevspace.joaovazstudio.data.Task
import com.comunidadedevspace.joaovazstudio.data.TaskDao

class ExerciseListViewModel(private val taskDao: TaskDao, private val currentUserId: Long) : ViewModel() {

    val taskListLiveData: LiveData<List<Task>> = taskDao.getTasksByUserId(currentUserId)

        companion object {

            fun create(application: Application, currentUserId: Long): ExerciseListViewModel {
            val dataBaseInstance = (application as JoaoVazStudio).getAppDataBase()
            val taskDao = dataBaseInstance.taskDao()
            return ExerciseListViewModel(taskDao, currentUserId)
        }
    }
}