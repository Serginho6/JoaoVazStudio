package com.comunidadedevspace.joaovazstudio.presentation.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.comunidadedevspace.joaovazstudio.JoaoVazStudio
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.data.local.User
import com.comunidadedevspace.joaovazstudio.data.local.UserDao
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ForgotPassword : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    private lateinit var emailEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val app = application as JoaoVazStudio
        userDao = app.getAppDataBase().userDao()

        emailEditText = findViewById(R.id.edt_email_forgot)

        val resetButton = findViewById<Button>(R.id.btn_reset_password)
        val backButton = findViewById<Button>(R.id.btn_back_to_login)

        resetButton.setOnClickListener {
            val email = emailEditText.text.toString()

            if (isValidEmail(email)) {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showSuccess("E-mail de redefinição de senha enviado. Verifique sua caixa de entrada.")
                            val intent = Intent(this@ForgotPassword, SignIn::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            showError("Erro ao enviar o e-mail de redefinição de senha. Verifique o e-mail fornecido.")
                        }
                    }
                    .addOnFailureListener { exception ->
                        showError("Erro ao enviar o e-mail de redefinição de senha: ${exception.message}")
                    }
            } else {
                showError("E-mail inválido")
            }
        }


        backButton.setOnClickListener {
            val intent = Intent(this@ForgotPassword, SignIn::class.java)
            startActivity(intent)
            finish()
        }
    }

    private suspend fun getUserByEmail(email: String): User? {
        return withContext(Dispatchers.IO) {
            userDao.getUserByEmail(email)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    private fun isValidPassword(password: String): Boolean {
        val minLength = 6
        val maxLength = 8

        return  password.length in minLength..maxLength
    }

    private fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}