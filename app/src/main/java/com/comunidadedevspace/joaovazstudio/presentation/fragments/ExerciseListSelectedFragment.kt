package com.comunidadedevspace.joaovazstudio.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.comunidadedevspace.joaovazstudio.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ExerciseListSelectedFragment : Fragment() {

    private var currentTrainId: String? = null
    private var currentUserId: String? = null

    private lateinit var fabSelectTrain: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exercise_list_selected, container, false)

        fabSelectTrain = view.findViewById(R.id.fab_select_train)
        val selectedTrainContent = view.findViewById<View>(R.id.selected_train_content)

        currentTrainId = arguments?.getString("currentTrainId")
        currentUserId = arguments?.getString("currentUserId")

        if (currentTrainId.isNullOrEmpty()) {
            selectedTrainContent.visibility = View.VISIBLE
        } else {
            selectedTrainContent.visibility = View.GONE
        }

        fabSelectTrain.setOnClickListener {
            // Exibir um diálogo de acordo com sua lógica aqui.
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(currentTrainId: String?, currentUserId: String?) = ExerciseListSelectedFragment().apply {
            arguments = Bundle().apply {
                putString("currentTrainId", currentTrainId)
                putString("currentUserId", currentUserId)
            }
        }
    }
}