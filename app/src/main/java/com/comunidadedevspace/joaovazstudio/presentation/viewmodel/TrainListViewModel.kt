package com.comunidadedevspace.joaovazstudio.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.comunidadedevspace.joaovazstudio.data.models.Train
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TrainListViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getTrainList(userUid: String): List<Train> {
        val querySnapshotTrain = db.collection("users")
            .document(userUid)
            .collection("treinos")
            .orderBy("nome")
            .get()
            .await()

        return querySnapshotTrain.toObjects(Train::class.java)
    }
}