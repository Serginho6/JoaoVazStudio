package com.comunidadedevspace.joaovazstudio.presentation.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.comunidadedevspace.joaovazstudio.R

class ImcActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imc)

        val btnCalcular: Button = findViewById(R.id.btnCalcular)
        val edtPeso: EditText = findViewById(R.id.edttext_peso)
        val edtAltura: EditText = findViewById(R.id.edttext_altura)

        btnCalcular.setOnClickListener{

            val alturaStr = edtAltura.text.toString()
            val pesoStr = edtPeso.text.toString()

            if(alturaStr.isNotEmpty() && pesoStr.isNotEmpty()){
                val altura: Float = alturaStr.toFloat()
                val peso: Float = pesoStr.toFloat()

                val alturaFinal: Float = altura * altura
                val result: Float = peso / alturaFinal

                val intent = Intent(this, ImcResultActivity::class.java)
                    .apply {
                        putExtra("EXTRA_RESULT", result)
                    }

                startActivity(intent)
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_LONG).show()
            }
        }
    }
}