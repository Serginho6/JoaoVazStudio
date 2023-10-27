package com.devserginho.joaovazstudio.data.firebase

import com.devserginho.joaovazstudio.data.models.User
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepository {

    private val db = FirebaseFirestore.getInstance()

    fun saveUserInfo(uid: String, user: User, callback: (Boolean) -> Unit) {
        db.collection("users").document(uid)
            .set(user)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }
}