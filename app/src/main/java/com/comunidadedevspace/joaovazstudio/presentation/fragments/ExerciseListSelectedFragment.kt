package com.comunidadedevspace.joaovazstudio.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment

class ExerciseListSelectedFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(currentTrainId: String?, userUid: String?) = ExerciseListSelectedFragment().apply {
            arguments = Bundle().apply {
                putString("currentTrainId", currentTrainId)
                putString("userUid", userUid)
            }
        }
    }
}