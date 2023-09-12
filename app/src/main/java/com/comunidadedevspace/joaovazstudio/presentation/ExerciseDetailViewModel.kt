package com.comunidadedevspace.joaovazstudio.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.comunidadedevspace.joaovazstudio.JoaoVazStudio
import com.comunidadedevspace.joaovazstudio.data.Task
import com.comunidadedevspace.joaovazstudio.data.TaskDao
import kotlinx.coroutines.launch

class ExerciseDetailViewModel(
    private val taskDao: TaskDao,
): ViewModel() {

    fun execute(taskAction: TaskAction){
        when (taskAction.actionType) {
            ActionType.DELETE.name -> deleteById(taskAction.task!!.id)
            ActionType.CREATE.name -> insertIntoDataBase(taskAction.task!!)
            ActionType.UPDATE.name -> updateIntoDataBase(taskAction.task!!)
        }
    }

    //CREATE
    private fun insertIntoDataBase(task: Task) {
        viewModelScope.launch {
            taskDao.insert(task)
        }
    }

    //DELETE BY ID
    private fun deleteById(id: Int) {
        viewModelScope.launch {
            taskDao.deleteById(id)
        }
    }

    //UPDATE
    private fun updateIntoDataBase(task: Task) {
        viewModelScope.launch {
            taskDao.update(task)
        }
    }

    companion object {

        fun getVMFactory(application: Application): ViewModelProvider.Factory {
            val dataBaseInstance = (application as JoaoVazStudio).getAppDataBase()
            val dao = dataBaseInstance.taskDao()
            val factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ExerciseDetailViewModel(dao) as T
                }
            }
            return factory
        }
    }
}