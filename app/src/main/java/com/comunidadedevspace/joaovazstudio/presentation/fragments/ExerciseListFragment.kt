package com.comunidadedevspace.joaovazstudio.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment

class ExerciseListFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(currentTrainId: String) = ExerciseListFragment().apply {
            arguments = Bundle().apply {
                putString("currentTrainId", currentTrainId)
            }
        }
    }
}