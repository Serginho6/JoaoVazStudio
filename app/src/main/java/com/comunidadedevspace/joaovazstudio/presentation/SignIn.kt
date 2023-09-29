package com.comunidadedevspace.joaovazstudio.presentation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
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
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.data.AppDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class LoginResult {
    data class Success(val userId: Long) : LoginResult()
    data class Error(val message: String) : LoginResult()
}

class SignIn : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    private lateinit var errorTextView: TextView

    private lateinit var keepConnectedCheckBox: CheckBox
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val btnLogin = findViewById<Button>(R.id.btn_login)

        errorTextView = findViewById(R.id.tv_error_signin)
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

        keepConnectedCheckBox = findViewById(R.id.checkbox_keep_connected)
        sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

        val isUserLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isUserLoggedIn) {
            val userId = sharedPreferences.getLong("userId", -1)
            if (userId != -1L) {
                val intent = Intent(this@SignIn, MainActivity::class.java)
                intent.putExtra("currentUserId", userId)
                startActivity(intent)
                finish() // Feche a tela de login
            }
        }

        // Troca imagem da tela de acordo com o tema.
        val imageView = findViewById<ImageView>(R.id.image_view_logo)

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            imageView.setImageResource(R.drawable.joao_vaz_icon_dark)
        } else {
            imageView.setImageResource(R.drawable.joao_vaz_icon_white)
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
                    val userId = result.userId // Obtenha o userId após o login bem-sucedido
                    sharedPreferences.edit().putLong("userId", userId).apply()

                    val intent = Intent(this@SignIn, MainActivity::class.java)
                    intent.putExtra("currentUserId", userId)
                    startActivity(intent)
                    runOnUiThread {
                        errorTextView.visibility = View.GONE

                        if (keepConnectedCheckBox.isChecked) {
                            sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                        } else {
                            sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
                        }
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
                val userId = user.id
                LoginResult.Success(userId)
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
