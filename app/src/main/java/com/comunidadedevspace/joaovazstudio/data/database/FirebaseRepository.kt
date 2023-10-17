package com.comunidadedevspace.joaovazstudio.data.database

import com.comunidadedevspace.joaovazstudio.data.local.User
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