package com.devserginho.joaovazstudio.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.devserginho.joaovazstudio.data.models.Exercise
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ExerciseListViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getExerciseList(userUid: String, trainId: String): List<Exercise> {
        val querySnapshotExercise = db.collection("users")
            .document(userUid)
            .collection("treinos")
            .document(trainId)
            .collection("exercicios")
            .orderBy("nome")
            .get()
            .await()

        return querySnapshotExercise.toObjects(Exercise::class.java)
    }
}