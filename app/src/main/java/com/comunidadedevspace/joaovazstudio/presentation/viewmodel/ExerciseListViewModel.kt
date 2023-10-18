package com.comunidadedevspace.joaovazstudio.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ExerciseListViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    fun getExerciseListQuery(userUid: String, trainId: String): Query {
        return db.collection("users")
            .document(userUid)
            .collection("trains")
            .document(trainId)
            .collection("exercises")
            .orderBy("title")
    }
}