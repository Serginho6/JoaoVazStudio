package com.comunidadedevspace.joaovazstudio.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.comunidadedevspace.joaovazstudio.JoaoVazStudio
import com.comunidadedevspace.joaovazstudio.data.Train
import com.comunidadedevspace.joaovazstudio.data.TrainDao
import kotlinx.coroutines.launch

class TrainDetailViewModel(
    private val trainDao: TrainDao,
    private val userId: Long,
): ViewModel() {

    fun execute(trainAction: TrainAction){
        when (trainAction.trainActionType) {
            ActionType.DELETE.name -> deleteById(trainAction.train!!.id, userId)
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
    private fun deleteById(id: Int, userId: Long) {
        viewModelScope.launch {
            trainDao.deleteById(id, userId)
        }
    }

    //UPDATE
    private fun updateIntoDataBase(train: Train) {
        viewModelScope.launch {
            trainDao.update(train)
        }
    }

    companion object {

        fun getVMFactory(application: Application, userId: Long): ViewModelProvider.Factory {
            val dataBaseInstance = (application as JoaoVazStudio).getAppDataBase()
            val trainDao = dataBaseInstance.trainDao()
            val factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TrainDetailViewModel(trainDao, userId) as T
                }
            }
            return factory
        }
    }
}