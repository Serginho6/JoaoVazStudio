package com.comunidadedevspace.joaovazstudio.presentation.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.comunidadedevspace.joaovazstudio.R

class ImcResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imc_result)

        val tvResult = findViewById<TextView>(R.id.textview_result)
        val tvClassificacao = findViewById<TextView>(R.id.textview_classificacao)

        val result = intent.getFloatExtra("EXTRA_RESULT", 0.1f)

        tvResult.text = result.toString()

        val classificacao = if(result < 18.5f) {
            "ABAIXO DO PESO"
        }else if(result in 18.5f..24.99f) {
            "PESO NORMAL"
        }else if(result in 25.0f..29.99f) {
            "ACIMA DO PESO"
        }else if(result in 30.0f..39.99f) {
            "OBESIDADE GRAU I"
        }else{
            "OBESIDADE GRAU II"
        }

        tvClassificacao.text = getString(R.string.message_classificacao,classificacao)

        val btnImcVoltar = findViewById<Button>(R.id.btnImcVoltar)

        btnImcVoltar.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}