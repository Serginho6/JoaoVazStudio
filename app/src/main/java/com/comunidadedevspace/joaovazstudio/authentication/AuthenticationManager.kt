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

    // Implemente aqui a função de login com email e senha, se ainda não estiver implementada.
    fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Autenticação bem-sucedida, o usuário está logado
                    val user = auth.currentUser
                    // Você pode executar ações adicionais aqui, se necessário
                } else {
                    // A autenticação falhou, trate o erro conforme necessário
                    val exception = task.exception
                    // Exiba uma mensagem de erro para o usuário, registre o erro, etc.
                }
            }
    }

    // Implemente aqui a função de logout, se ainda não estiver implementada.
    fun signOut() {
        auth.signOut()
    }
}
