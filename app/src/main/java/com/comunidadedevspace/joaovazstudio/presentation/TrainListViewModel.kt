package com.comunidadedevspace.joaovazstudio.presentation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.comunidadedevspace.joaovazstudio.JoaoVazStudio
import com.comunidadedevspace.joaovazstudio.data.Train
import com.comunidadedevspace.joaovazstudio.data.TrainDao

class TrainListViewModel(private val trainDao: TrainDao, private val currentTrainId: Long) : ViewModel() {

    val trainListLiveData: LiveData<List<Train>> = trainDao.getTrainByUserId(currentTrainId)

        companion object {

            fun create(application: Application, currentTrainId: Long): TrainListViewModel {
            val dataBaseInstance = (application as JoaoVazStudio).getAppDataBase()
            val trainDao = dataBaseInstance.trainDao()
            return TrainListViewModel(trainDao, currentTrainId)
        }
    }
}