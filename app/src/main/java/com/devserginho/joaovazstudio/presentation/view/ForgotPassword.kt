package com.devserginho.joaovazstudio.presentation.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devserginho.joaovazstudio.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    private lateinit var emailEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

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

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    private fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}