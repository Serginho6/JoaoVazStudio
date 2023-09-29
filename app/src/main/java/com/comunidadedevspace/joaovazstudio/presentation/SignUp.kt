package com.comunidadedevspace.joaovazstudio.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.data.AppDataBase
import com.comunidadedevspace.joaovazstudio.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUp : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var heightEditText: EditText
    private lateinit var weightEditText: EditText
    private var gender: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

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
        registerButton.setOnClickListener {
            if (isInputValid()) {
                saveUserToDatabase()
            } else {
                showErrorMessage("É necessário preencher todos os campos.")
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

        return name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() &&
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
}