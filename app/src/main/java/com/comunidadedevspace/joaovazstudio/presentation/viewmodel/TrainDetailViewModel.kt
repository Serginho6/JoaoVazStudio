package com.comunidadedevspace.joaovazstudio.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.comunidadedevspace.joaovazstudio.JoaoVazStudio
import com.comunidadedevspace.joaovazstudio.data.database.ActionType
import com.comunidadedevspace.joaovazstudio.data.database.TrainAction
import com.comunidadedevspace.joaovazstudio.data.local.Train
import com.comunidadedevspace.joaovazstudio.data.local.TrainDao
import kotlinx.coroutines.launch

class TrainDetailViewModel(
    private val trainDao: TrainDao,
): ViewModel() {

    fun execute(trainAction: TrainAction){
        when (trainAction.trainActionType) {
            ActionType.DELETE.name -> deleteById(trainAction.train!!.id)
            ActionType.CREATE.name -> insertIntoDataBase(trainAction.train!!)
            ActionType.UPDATE.name -> updateIntoDataBase(trainAction.train!!)
        }
    }

    //CREATE
    private fun insertIntoDataBase(train: Train) {
        viewModelScope.launch {
            trainDao.insert(train)
        }
    }

    //DELETE BY ID
    private fun deleteById(id: String) {
        viewModelScope.launch {
            trainDao.deleteById(id)
        }
    }

    //UPDATE
    private fun updateIntoDataBase(train: Train) {
        viewModelScope.launch {
            trainDao.update(train)
        }
    }

    companion object {

        fun getVMFactory(application: Application): ViewModelProvider.Factory {
            val dataBaseInstance = (application as JoaoVazStudio).getAppDataBase()
            val trainDao = dataBaseInstance.trainDao()
            val factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TrainDetailViewModel(trainDao) as T
                }
            }
            return factory
        }
    }
}