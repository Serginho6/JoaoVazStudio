package com.comunidadedevspace.joaovazstudio.presentation.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.data.local.AppDataBase
import com.comunidadedevspace.joaovazstudio.data.local.User
import com.comunidadedevspace.joaovazstudio.databinding.ActivitySignUpBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val auth = FirebaseAuth.getInstance()

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var heightEditText: EditText
    private lateinit var weightEditText: EditText
    private var gender: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nameEditText = findViewById(R.id.name_edt_text)
        emailEditText = findViewById(R.id.email_edt_text)
        passwordEditText = findViewById(R.id.password_edt_text)
        phoneEditText = findViewById(R.id.phone_edt_text)
        heightEditText = findViewById(R.id.height_edt_text)
        weightEditText = findViewById(R.id.weight_edt_text)

        val maleRadioButton = findViewById<RadioButton>(R.id.male_radio)
        val femaleRadioButton = findViewById<RadioButton>(R.id.female_radio)

        maleRadioButton.isChecked = false
        femaleRadioButton.isChecked = false

        maleRadioButton.setOnClickListener {
            maleRadioButton.isChecked = true
            femaleRadioButton.isChecked = false
            gender = "Masculino"
        }

        femaleRadioButton.setOnClickListener {
            maleRadioButton.isChecked = false
            femaleRadioButton.isChecked = true
            gender = "Feminino"
        }

        val registerButton = findViewById<Button>(R.id.btn_register)
        registerButton.setOnClickListener { view ->
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

//            if (!isValidEmail(email)) {
//                showErrorMessage("Endereço de e-mail inválido")
//            } else if (!isValidPassword(password)) {
//                showErrorMessage("Senha deve conter de 6 à 8 dígitos")
//            } else if (isInputValid()) {
//                saveUserToDatabase()
//            } else {
//                showErrorMessage("É necessário preencher todos os campos.")
//            }

            if (email.isEmpty() || password.isEmpty()) {
                val snackbar =
                    Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                    if (it.isSuccessful){
                        val snackbar =
                            Snackbar.make(view, "Usuário Cadastrado", Snackbar.LENGTH_SHORT)
                        val backgroundColor = Color.parseColor("#FF03DAC5")
                        snackbar.setBackgroundTint(backgroundColor)
                        snackbar.show()
                        binding.emailEdtText.setText("")
                        binding.passwordEdtText.setText("")
                    }
                }.addOnFailureListener{exception ->
                    val errorMessage = when(exception){
                        is FirebaseAuthWeakPasswordException -> "Digite uma senha com no mínimo 6 caracteres."
                        is FirebaseAuthInvalidCredentialsException -> "Digite um email válido."
                        is FirebaseAuthUserCollisionException -> "E-mail já cadastrado."
                        is FirebaseNetworkException -> "Sem conexão com a internet."
                        else -> "Erro ao cadastrar usuário."
                    }
                    val snackbar =
                        Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(Color.RED)
                    snackbar.show()
                }
            }
        }
    }

    private fun isInputValid(): Boolean {
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val phone = phoneEditText.text.toString()
        val gender = gender.isNotEmpty()
        val height = heightEditText.text.toString()
        val weight = weightEditText.text.toString()

        val isEmailValid = isValidEmail(email)
        val isPasswordValid = isValidPassword(password)

        return name.isNotEmpty() && isEmailValid && email.isNotEmpty() && isPasswordValid && password.isNotEmpty() &&
                phone.isNotEmpty() && gender && height.isNotEmpty() && weight.isNotEmpty()
    }

    private fun saveUserToDatabase() {
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val phone = phoneEditText.text.toString()
        val height = heightEditText.text.toString()
        val weight = weightEditText.text.toString()

        val user = User(
            name = name,
            email = email,
            password = password,
            phone = phone,
            gender = gender,
            height = height,
            weight = weight
        )

        lifecycleScope.launch(Dispatchers.IO) {
            val userDao = Room.databaseBuilder(
                applicationContext,
                AppDataBase::class.java,
                "joao-vaz-studio-database"
            ).build().userDao()

            userDao.insert(user)

            runOnUiThread {
                clearEditTextFields()
                navigateToSignIn()
            }
        }
    }

    private fun clearEditTextFields() {
        nameEditText.text.clear()
        emailEditText.text.clear()
        passwordEditText.text.clear()
        phoneEditText.text.clear()
        heightEditText.text.clear()
        weightEditText.text.clear()
    }

    private fun navigateToSignIn() {
        val intent = Intent(this@SignUp, SignIn::class.java)
        startActivity(intent)
    }

    private fun showErrorMessage(message: String) {
        val errorMessageTextView = findViewById<TextView>(R.id.tv_error_signup)
        errorMessageTextView.visibility = View.VISIBLE
        errorMessageTextView.text = message
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        val minLength = 6
        val maxLength = 8

        return  password.length in minLength..maxLength
    }


}