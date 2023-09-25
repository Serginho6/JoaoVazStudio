package com.comunidadedevspace.joaovazstudio.presentation

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.comunidadedevspace.joaovazstudio.R

class MoreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_more, container, false)

        val cardViewImc = view.findViewById<CardView>(R.id.card_view_imc)
        val cardViewPhysical = view.findViewById<CardView>(R.id.card_view_physical_assessment)
        val cardViewTrain = view.findViewById<CardView>(R.id.card_view_train)
        val cardViewProfile = view.findViewById<CardView>(R.id.card_view_item_profile)
        val cardViewAbout = view.findViewById<CardView>(R.id.card_view_about)
        val cardViewLogout = view.findViewById<CardView>(R.id.card_view_logout)

        cardViewImc.setOnClickListener {
            val intent = Intent(activity, ImcActivity::class.java)
            startActivity(intent)
        }

        cardViewPhysical.setOnClickListener {
//            val intent = Intent(activity, PhysicalActivity::class.java)
//            startActivity(intent)
            Toast.makeText(context, "Em breve", Toast.LENGTH_SHORT).show()
        }

        cardViewTrain.setOnClickListener {
            val intent = Intent(activity, TrainListActivity::class.java)
            startActivity(intent)
        }

        cardViewProfile.setOnClickListener {
//            val intent = Intent(activity, SignUp::class.java)
//            startActivity(intent)
            Toast.makeText(context, "Em breve", Toast.LENGTH_SHORT).show()
        }

        cardViewAbout.setOnClickListener {
            val intent = Intent(activity, AboutActivity::class.java)
            startActivity(intent)
        }

        cardViewLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        return view
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirmação de Logout")
        builder.setMessage("Tem certeza que deseja sair?")

        builder.setPositiveButton("Sim") { _, _ ->
            val sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
            val intent = Intent(requireActivity(), SignIn::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        builder.setNegativeButton("Não") { _, _ ->
            // Não faz nada, apenas fecha o diálogo
        }

        val dialog = builder.create()
        dialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MoreFragment()
    }
}