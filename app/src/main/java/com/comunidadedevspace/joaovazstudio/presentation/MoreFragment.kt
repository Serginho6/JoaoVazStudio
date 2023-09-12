package com.comunidadedevspace.joaovazstudio.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        cardViewImc.setOnClickListener {
            val intent = Intent(activity, ImcActivity::class.java)
            startActivity(intent)
        }

        cardViewPhysical.setOnClickListener {
            val intent = Intent(activity, PhysicalActivity::class.java)
            startActivity(intent)
        }

        cardViewTrain.setOnClickListener {
            val intent = Intent(activity, TrainActivity::class.java)
            startActivity(intent)
        }

        cardViewProfile.setOnClickListener {
            val intent = Intent(activity, SignUp::class.java)
            startActivity(intent)
        }

        cardViewAbout.setOnClickListener {
            val intent = Intent(activity, AboutActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = MoreFragment()
    }
}