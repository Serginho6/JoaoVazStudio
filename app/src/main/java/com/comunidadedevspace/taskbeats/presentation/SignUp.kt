package com.comunidadedevspace.taskbeats.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.comunidadedevspace.taskbeats.R
import com.comunidadedevspace.taskbeats.data.AppDataBase
import com.comunidadedevspace.taskbeats.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUp : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var heightEditText: EditText
    private lateinit var weightEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val registerButton = findViewById<Button>(R.id.btn_register)

        nameEditText = findViewById(R.id.name_edt_text)
        emailEditText = findViewById(R.id.email_edt_text)
        passwordEditText = findViewById(R.id.password_edt_text)
        phoneEditText = findViewById(R.id.phone_edt_text)
        heightEditText = findViewById(R.id.height_edt_text)
        weightEditText = findViewById(R.id.weight_edt_text)

        registerButton.setOnClickListener {
            saveUserToDatabase()
        }
    }

    private fun saveUserToDatabase() {
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val phone = phoneEditText.text.toString()

        val selectedGender = findViewById<RadioButton>(
            findViewById<RadioGroup>(R.id.gender_radio_group).checkedRadioButtonId
        )?.text.toString()

        val height = heightEditText.text.toString().toDouble()
        val weight = weightEditText.text.toString().toDouble()

        val user = User(
            name = name,
            email = email,
            password = password,
            phone = phone,
            gender = selectedGender,
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

                val intent = Intent(this@SignUp, SignIn::class.java)
                startActivity(intent)
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

    fun onRadioButtonClicked(view: View) {
        val isSelected = (view as RadioButton).isChecked
        val maleRadioButton = findViewById<RadioButton>(R.id.male_radio)
        val femaleRadioButton = findViewById<RadioButton>(R.id.female_radio)

        when (view.id) {
            R.id.female_radio -> {
                if (isSelected) {
                    maleRadioButton.isChecked = false
                }
            }

            R.id.male_radio -> {
                if (isSelected) {
                    femaleRadioButton.isChecked = false
                }
            }
        }
    }
}


//Mostra uma mensagem ao clicar no botão de salvar.

//    private fun showAlertDialog() {
//        val alertDialog = AlertDialog.Builder(this)
//            .setTitle("Tem certeza que deseja sair?")
//            .setMessage("Seus dados não serão salvos.")
//            .setPositiveButton("Sim") { dialog, which ->
//                finish()
//            }
//            .setNegativeButton("Não") { dialog, which -> }
//            .create()
//        alertDialog.show()
//    }

//    private fun isPasswordValid(password: String): Boolean{
//        val hasUpperCase = password.any{
//            it.isUpperCase()
//        }
//
//        val hasLowerCase = password.any{
//            it.isLowerCase()
//        }
//
//        val hasDigit = password.any{
//            it.isDigit()
//        }
//
//        val isLenghtValid = password.length >= 8
//
//        return hasUpperCase && hasLowerCase && hasDigit && isLenghtValid
//    }