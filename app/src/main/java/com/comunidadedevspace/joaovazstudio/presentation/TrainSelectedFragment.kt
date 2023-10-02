package com.comunidadedevspace.joaovazstudio.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.comunidadedevspace.joaovazstudio.R

class TrainSelectedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_selected_train, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = TrainSelectedFragment()
    }
}