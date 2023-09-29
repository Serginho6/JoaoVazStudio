package com.comunidadedevspace.joaovazstudio.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.comunidadedevspace.joaovazstudio.JoaoVazStudio
import com.comunidadedevspace.joaovazstudio.data.Exercise
import com.comunidadedevspace.joaovazstudio.data.ExerciseDao
import kotlinx.coroutines.launch

class ExerciseDetailViewModel(
    private val exerciseDao: ExerciseDao,
): ViewModel() {

    fun execute(taskAction: TaskAction){
        when (taskAction.taskActionType) {
            ActionType.DELETE.name -> deleteById(taskAction.exercise!!.id)
            ActionType.CREATE.name -> insertIntoDataBase(taskAction.exercise!!)
            ActionType.UPDATE.name -> updateIntoDataBase(taskAction.exercise!!)
        }
    }

    //CREATE
    private fun insertIntoDataBase(exercise: Exercise) {
        viewModelScope.launch {
            exerciseDao.insert(exercise)
        }
    }

    //DELETE BY ID
    private fun deleteById(id: Int) {
        viewModelScope.launch {
            exerciseDao.deleteById(id)
        }
    }

    //UPDATE
    private fun updateIntoDataBase(exercise: Exercise) {
        viewModelScope.launch {
            exerciseDao.update(exercise)
        }
    }

    companion object {

        fun getVMFactory(application: Application): ViewModelProvider.Factory {
            val dataBaseInstance = (application as JoaoVazStudio).getAppDataBase()
            val dao = dataBaseInstance.exerciseDao()
            val factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ExerciseDetailViewModel(dao) as T
                }
            }
            return factory
        }
    }
}