package com.comunidadedevspace.joaovazstudio.presentation.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.comunidadedevspace.joaovazstudio.R

class ImcResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imc_result)

        val tvResult = findViewById<TextView>(R.id.tv_result)
        val tvClassificacao = findViewById<TextView>(R.id.tv_classificacao)

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

        when (classificacao) {
            "ABAIXO DO PESO" -> tvClassificacao.setTextColor(ContextCompat.getColor(this, R.color.AbaixoDoPeso))
            "PESO NORMAL" -> tvClassificacao.setTextColor(ContextCompat.getColor(this, R.color.PesoNormal))
            "ACIMA DO PESO" -> tvClassificacao.setTextColor(ContextCompat.getColor(this, R.color.AcimaDoPeso))
            "OBESIDADE GRAU I" -> tvClassificacao.setTextColor(ContextCompat.getColor(this, R.color.ObesidadeGrauI))
            "OBESIDADE GRAU II" -> tvClassificacao.setTextColor(ContextCompat.getColor(this, R.color.ObesidadeGrauII))
        }

        val btnImcVoltar = findViewById<Button>(R.id.btnImcVoltar)

        btnImcVoltar.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}