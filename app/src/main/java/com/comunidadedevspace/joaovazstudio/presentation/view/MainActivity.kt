package com.comunidadedevspace.joaovazstudio.presentation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.comunidadedevspace.joaovazstudio.R
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = FirebaseFirestore.getInstance()

        val userUid = intent.getStringExtra("userUid")
        val textViewToolbar = findViewById<TextView>(R.id.text_view_toolbar)

        if (userUid != null) {
            db.collection("users")
                .document(userUid)
                .get()
                .addOnSuccessListener { documentSnapshot: DocumentSnapshot? ->
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        val userName = documentSnapshot.getString("name")
                        if (!userName.isNullOrBlank()) {
                            textViewToolbar.text = "Olá, $userName"
                        }
                    }
                }
        }

        val cardViewImc = findViewById<CardView>(R.id.card_view_imc)
//        val cardViewPhysical = findViewById<CardView>(R.id.card_view_physical_assessment)
        val cardViewTrain = findViewById<CardView>(R.id.card_view_train)
//        val cardViewProfile = findViewById<CardView>(R.id.card_view_profile)
        val cardViewAbout = findViewById<CardView>(R.id.card_view_about)
        val cardViewLogout = findViewById<CardView>(R.id.card_view_logout)

        cardViewTrain.setOnClickListener {
            val intent = Intent(this, TrainListActivity::class.java)
            intent.putExtra("currentUserId", userUid)
            startActivity(intent)
        }

        cardViewImc.setOnClickListener {
            val intent = Intent(this, ImcActivity::class.java)
            intent.putExtra("currentUserId", userUid)
            startActivity(intent)
        }

//        cardViewPhysical.setOnClickListener {
//            val intent = Intent(this, PhysicalActivity::class.java)
//            intent.putExtra("currentUserId", currentUserId)
//            startActivity(intent)
//            Toast.makeText(this, "Em breve", Toast.LENGTH_SHORT).show()
//        }
//
//        cardViewProfile.setOnClickListener {
//            val intent = Intent(this, SignUp::class.java)
//            intent.putExtra("currentUserId", currentUserId)
//            startActivity(intent)
//            Toast.makeText(this, "Em breve", Toast.LENGTH_SHORT).show()
//        }

        cardViewAbout.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        cardViewLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmação de Logout")
        builder.setMessage("Tem certeza que deseja sair?")

        builder.setPositiveButton("Sim") { _, _ ->
            val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
            val intent = Intent(this, SignIn::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        builder.setNegativeButton("Não") { _, _ ->
            // Não faz nada, apenas fecha o diálogo
        }

        val dialog = builder.create()
        dialog.show()
    }
}
