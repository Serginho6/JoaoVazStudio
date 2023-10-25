package com.comunidadedevspace.joaovazstudio.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.comunidadedevspace.joaovazstudio.data.models.Exercise
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ExerciseListViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getExerciseList(userUid: String, trainId: String): List<Exercise> {
        val querySnapshotExercise = db.collection("users")
            .document(userUid)
            .collection("trains")
            .document(trainId)
            .collection("exercises")
            .orderBy("title")
            .get()
            .await()

        return querySnapshotExercise.toObjects(Exercise::class.java)
    }
}