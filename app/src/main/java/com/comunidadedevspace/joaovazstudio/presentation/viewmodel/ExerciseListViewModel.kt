package com.comunidadedevspace.joaovazstudio.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.comunidadedevspace.joaovazstudio.JoaoVazStudio
import com.comunidadedevspace.joaovazstudio.data.local.Exercise
import com.comunidadedevspace.joaovazstudio.data.local.ExerciseDao

class ExerciseListViewModel(private val exerciseDao: ExerciseDao, private val currentTrainId: Int) : ViewModel() {

    val exerciseListLiveData: LiveData<List<Exercise>> = exerciseDao.getExercisesByTrainId(currentTrainId)

        companion object {

            fun create(application: Application, currentTrainId: Int): ExerciseListViewModel {
            val dataBaseInstance = (application as JoaoVazStudio).getAppDataBase()
            val exerciseDao = dataBaseInstance.exerciseDao()
            return ExerciseListViewModel(exerciseDao, currentTrainId)
        }
    }
}