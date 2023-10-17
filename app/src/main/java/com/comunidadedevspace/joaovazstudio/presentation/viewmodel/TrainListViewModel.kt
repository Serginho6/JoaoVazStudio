package com.comunidadedevspace.joaovazstudio.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class TrainListViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    fun getTrainListQuery(userUid: String): Query {
        return db.collection("users")
            .document(userUid)
            .collection("trains")
            .orderBy("trainTitle")
    }
}