package com.comunidadedevspace.taskbeats.presentation

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.comunidadedevspace.taskbeats.R
import com.comunidadedevspace.taskbeats.data.AppDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class LoginResult {
    object Success : LoginResult()
    data class Error(val message: String) : LoginResult()
}

class SignIn : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    private lateinit var errorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val btnLogin = findViewById<Button>(R.id.btn_login)

        errorTextView = findViewById(R.id.textview_error)
        errorTextView.visibility = View.GONE

        emailEditText = findViewById(R.id.email_edt_text)
        passwordEditText = findViewById(R.id.password_edt_text)

        val passwordToggleButton = findViewById<ImageButton>(R.id.password_toggle_button)

        // Inicialmente, a senha está oculta
        passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()

        passwordToggleButton.setOnClickListener {
            // Alternar a visibilidade da senha quando o botão for clicado
            if (passwordEditText.transformationMethod == PasswordTransformationMethod.getInstance()) {
                passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
                passwordToggleButton.setImageResource(R.drawable.ic_visibility) // Ícone para senha visível
            } else {
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
                passwordToggleButton.setImageResource(R.drawable.ic_visibility_off) // Ícone para senha oculta
            }

            // Mova o cursor para o final do texto
            passwordEditText.setSelection(passwordEditText.text.length)
        }

        btnLogin.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            attemptLogin(email, password)
        }

        val registreSeAquiTextView = findViewById<TextView>(R.id.textview_link)
        val fullText = getString(R.string.textNewUser)

        val startIndex = fullText.indexOf("Registre-se aqui")
        val endIndex = startIndex + "Registre-se aqui".length

        val spannableString = SpannableString(fullText)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@SignIn, SignUp::class.java)
                startActivity(intent)
            }
        }

        val clickableColor = ContextCompat.getColor(this, R.color.green_300)

        spannableString.setSpan(
            clickableSpan,
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(clickableColor),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        registreSeAquiTextView.text = spannableString
        registreSeAquiTextView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun attemptLogin(email: String, password: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            // Verificando as credenciais no banco de dados
            when (val result = isValidCredentials(email, password)) {
                is LoginResult.Success -> {
                    val intent = Intent(this@SignIn, MainActivity::class.java)
                    startActivity(intent)
                    runOnUiThread {
                        errorTextView.visibility = View.GONE
                    }
                }
                is LoginResult.Error -> {
                    showError(result.message)
                }
            }
        }
    }

    private suspend fun isValidCredentials(email: String, password: String): LoginResult {
        return try {
            val userDao = Room.databaseBuilder(
                applicationContext,
                AppDataBase::class.java,
                "joao-vaz-studio-database"
            ).build().userDao()

            val user = userDao.getUserByEmail(email)

            if (user != null && user.password == password) {
                LoginResult.Success
            } else {
                LoginResult.Error("Email ou Senha incorreto(s)")
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            LoginResult.Error("Preencha todos os campos")
        }
    }

    private fun showError(message: String) {
        runOnUiThread {
            errorTextView.text = message
            errorTextView.visibility = View.VISIBLE
        }
    }
}