package com.comunidadedevspace.taskbeats.presentation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comunidadedevspace.taskbeats.TaskBeatsApplication
import com.comunidadedevspace.taskbeats.data.Task
import com.comunidadedevspace.taskbeats.data.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskListViewModel(private val taskDao: TaskDao): ViewModel() {

    val taskListLiveData: LiveData<List<Task>> = taskDao.getAll()

    fun execute(taskAction: MainActivity.TaskAction){
        when (taskAction.actionType) {
            MainActivity.ActionType.DELETE.name -> deleteById(taskAction.task!!.id)
            MainActivity.ActionType.CREATE.name -> insertIntoDataBase(taskAction.task!!)
            MainActivity.ActionType.UPDATE.name -> updateIntoDataBase(taskAction.task!!)
            MainActivity.ActionType.DELETE_ALL.name -> deleteAll()
        }
    }

    //CREATE
    private fun insertIntoDataBase(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.insert(task)
        }
    }

    //DELETE BY ID
    private fun deleteById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.deleteById(id)
        }
    }

    //UPDATE
    private fun updateIntoDataBase(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.update(task)
        }
    }

    //DELETE ALL
    private fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.deleteAll()
        }
    }
        companion object {

            fun create(application: Application): TaskListViewModel {
            val dataBaseInstance = (application as TaskBeatsApplication).getAppDataBase()
            val dao = dataBaseInstance.taskDao()
            return TaskListViewModel(dao)
        }
    }
}