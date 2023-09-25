package com.comunidadedevspace.joaovazstudio.authentication

import com.google.firebase.auth.FirebaseAuth

object AuthenticationManager {
    private val auth = FirebaseAuth.getInstance()

    // Esta função retorna o ID do usuário atualmente logado, se houver algum, ou null caso contrário.
    fun getCurrentUserId(): Long {
        val currentUser = auth.currentUser
        return if (currentUser != null && currentUser.isEmailVerified) {
            currentUser.uid.toLong()
        } else {
            -1
        }
    }
}
