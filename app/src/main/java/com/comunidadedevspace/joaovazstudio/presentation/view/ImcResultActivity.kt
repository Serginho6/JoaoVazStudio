package com.comunidadedevspace.joaovazstudio.presentation.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import com.comunidadedevspace.joaovazstudio.R

class ImcResultActivity : AppCompatActivity() {

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imc_result)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tvResult = findViewById<TextView>(R.id.textview_result)
        val tvClassificacao = findViewById<TextView>(R.id.textview_classificacao)

        val result = intent.getFloatExtra("EXTRA_RESULT", 0.1f)

        tvResult.text = result.toString()

        var classificacao = if(result < 18.5f) {
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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}