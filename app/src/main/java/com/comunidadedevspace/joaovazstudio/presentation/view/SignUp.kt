package com.comunidadedevspace.joaovazstudio.presentation.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.data.firebase.FirebaseRepository
import com.comunidadedevspace.joaovazstudio.data.models.User
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

    private val auth = FirebaseAuth.getInstance()
    private lateinit var binding: ActivitySignUpBinding

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
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val height = heightEditText.text.toString()
            val weight = weightEditText.text.toString()

            if (name.isEmpty() ||email.isEmpty() || password.isEmpty() || phone.isEmpty() || gender.isEmpty() || height.isEmpty() || weight.isEmpty()) {
                val snackbar =
                    Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = task.result?.user
                        if (firebaseUser != null) {
                            val uid = firebaseUser.uid
                            // Agora, você tem o uid do usuário criado com sucesso, que pode ser usado para salvar informações adicionais no Firebase.
                            saveAdditionalUserInfo(uid, name, phone, gender, height, weight)
                        }
                    }
                }.addOnFailureListener { exception ->
                    val errorMessage = when (exception) {
                        is FirebaseAuthWeakPasswordException -> "Sua senha deve conter de 6 à 8 dígitos."
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

    private fun saveAdditionalUserInfo(uid: String, name: String, phone: String, gender: String, height: String, weight: String) {
        val user = User(
            id = 0,
            name = name,
            email = emailEditText.text.toString(),
            password = passwordEditText.text.toString(),
            phone = phone,
            gender = gender,
            height = height,
            weight = weight
        )

        FirebaseRepository().saveUserInfo(uid, user) { success ->
            if (success) {
                runOnUiThread {
                    clearEditTextFields()
                    navigateToSignIn()
                }
            } else {
                // Aqui você pode adicionar um Snackbar ou tratamento de erro, se necessário
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
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
}