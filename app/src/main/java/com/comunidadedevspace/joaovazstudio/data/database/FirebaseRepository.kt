package com.comunidadedevspace.joaovazstudio.data.database

import com.comunidadedevspace.joaovazstudio.data.local.Train
import com.comunidadedevspace.joaovazstudio.data.local.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepository {
    private val db = FirebaseFirestore.getInstance()

    fun saveUserInfo(uid: String, user: User, callback: (Boolean) -> Unit) {
        db.collection("users").document(uid)
            .set(user)
            .addOnSuccessListener {
                // Dados do usuário salvos com sucesso
                callback(true)
            }
            .addOnFailureListener {
                // Lidar com erros ao salvar os dados
                callback(false)
            }
    }

    fun saveTrain(train: Train, callback: (Boolean) -> Unit)  {
        val userUid = FirebaseAuth.getInstance().currentUser?.uid

        if (userUid != null) {
            db.collection("users").document(userUid)
                .collection("trains")
                .add(train)
                .addOnSuccessListener {
                    // Dados do usuário salvos com sucesso
                    callback(true)
                }
                .addOnFailureListener {
                    // Lidar com erros ao salvar os dados
                    callback(false)
                }
        } else {
            // O usuário não está autenticado, então você pode retornar um valor de erro
            callback(false)
        }
    }
}