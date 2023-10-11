package com.comunidadedevspace.joaovazstudio.presentation.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.comunidadedevspace.joaovazstudio.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class SignIn : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

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
                passwordEditText.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
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

        val forgotPasswordLink = findViewById<TextView>(R.id.forgot_password_link)

        forgotPasswordLink.setOnClickListener {
            val intent = Intent(this@SignIn, ForgotPassword::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener { view ->
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                val snackbar =
                    Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { loginResult ->
                        if (loginResult.isSuccessful) {

                            val editor = sharedPreferences.edit()
                            if (keepConnectedCheckBox.isChecked) {
                                editor.putBoolean("isLoggedIn", true)
                            } else {
                                editor.putBoolean("isLoggedIn", false)
                            }
                            editor.apply()

                            val intent = Intent(this@SignIn, MainActivity::class.java)
                            startActivity(intent)

                            val snackbar =
                                Snackbar.make(view, "Login bem-sucedido", Snackbar.LENGTH_SHORT)
                            val backgroundColor = Color.parseColor("#FF03DAC5")
                            snackbar.setBackgroundTint(backgroundColor)
                            snackbar.show()
                        }
                    }.addOnFailureListener { exception ->
                    val errorMessage = when (exception) {
                        is FirebaseAuthInvalidUserException -> "Usuário não encontrado. Verifique o e-mail fornecido."
                        is FirebaseAuthInvalidCredentialsException -> "Senha inválida. Verifique ou recupere sua senha."
                        is FirebaseNetworkException -> "Sem conexão com a internet."
                        else -> "Erro ao fazer login. Verifique e-mail e senha."
                    }
                    val snackbar = Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(Color.RED)
                    snackbar.show()
                }
            }
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

        val clickableColor = ContextCompat.getColor(this, R.color.sea_green)

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
}