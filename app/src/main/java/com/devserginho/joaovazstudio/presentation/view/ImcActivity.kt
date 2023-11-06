package com.devserginho.joaovazstudio.presentation.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devserginho.joaovazstudio.R
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class ImcActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imc)

        db = FirebaseFirestore.getInstance()

        val btnCalcular: Button = findViewById(R.id.btnImcCalcular)
        val edtPeso: EditText = findViewById(R.id.edt_text_peso)
        val edtAltura: EditText = findViewById(R.id.edt_text_altura)

        val userUid = intent.getStringExtra("currentUserId")

        if (userUid != null) {
            db.collection("users")
                .document(userUid)
                .get()
                .addOnSuccessListener { documentSnapshot: DocumentSnapshot? ->
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        val userHeight = documentSnapshot.get("height")
                        val userWeight = documentSnapshot.get("weight")

                        // Verifique se os valores não são nulos antes de preencher os campos
                        if (userHeight != null) {
                            edtAltura.setText(userHeight.toString())
                        }
                        if (userWeight != null) {
                            edtPeso.setText(userWeight.toString())
                        }
                    }
                }
        }

        btnCalcular.setOnClickListener{

            val alturaStr = edtAltura.text.toString()
            val pesoStr = edtPeso.text.toString()

            if(alturaStr.isNotEmpty() && pesoStr.isNotEmpty()){
                val altura: Float = alturaStr.toFloat()
                val peso: Float = pesoStr.toFloat()

                val alturaFinal: Float = altura/100 * altura/100
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