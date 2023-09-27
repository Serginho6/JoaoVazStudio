package com.comunidadedevspace.joaovazstudio.presentation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.comunidadedevspace.joaovazstudio.JoaoVazStudio
import com.comunidadedevspace.joaovazstudio.data.Exercise
import com.comunidadedevspace.joaovazstudio.data.ExerciseDao

class ExerciseListViewModel(private val exerciseDao: ExerciseDao, private val currentUserId: Long) : ViewModel() {

    val exerciseListLiveData: LiveData<List<Exercise>> = exerciseDao.getTasksByUserId(currentUserId)

        companion object {

            fun create(application: Application, currentUserId: Long): ExerciseListViewModel {
            val dataBaseInstance = (application as JoaoVazStudio).getAppDataBase()
            val exerciseDao = dataBaseInstance.exerciseDao()
            return ExerciseListViewModel(exerciseDao, currentUserId)
        }
    }
}