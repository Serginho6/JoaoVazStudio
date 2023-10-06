package com.comunidadedevspace.joaovazstudio.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.comunidadedevspace.joaovazstudio.JoaoVazStudio
import com.comunidadedevspace.joaovazstudio.data.database.ActionType
import com.comunidadedevspace.joaovazstudio.data.database.ExerciseAction
import com.comunidadedevspace.joaovazstudio.data.local.Exercise
import com.comunidadedevspace.joaovazstudio.data.local.ExerciseDao
import kotlinx.coroutines.launch

class ExerciseDetailViewModel(
    private val exerciseDao: ExerciseDao,
): ViewModel() {

    fun execute(exerciseAction: ExerciseAction){
        when (exerciseAction.taskActionType) {
            ActionType.DELETE.name -> deleteById(exerciseAction.exercise!!.id)
            ActionType.CREATE.name -> insertIntoDataBase(exerciseAction.exercise!!)
            ActionType.UPDATE.name -> updateIntoDataBase(exerciseAction.exercise!!)
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
            val exerciseDao = dataBaseInstance.exerciseDao()
            val factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ExerciseDetailViewModel(exerciseDao) as T
                }
            }
            return factory
        }
    }
}