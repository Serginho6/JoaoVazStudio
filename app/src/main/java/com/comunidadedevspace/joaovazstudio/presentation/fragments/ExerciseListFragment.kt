package com.comunidadedevspace.joaovazstudio.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.data.local.Exercise
import com.comunidadedevspace.joaovazstudio.presentation.adapters.ExerciseListAdapter
import com.comunidadedevspace.joaovazstudio.presentation.viewmodel.ExerciseListViewModel

class ExerciseListFragment : Fragment() {

    private var currentTrainId: Int = -1

    //Adapter
    private val exerciseAdapter: ExerciseListAdapter by lazy {
        ExerciseListAdapter(requireContext(), ::openExerciseListDetail)
    }

    private val exerciseViewModel: ExerciseListViewModel by lazy {
        ExerciseListViewModel.create(requireActivity().application, currentTrainId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exercise_list, container, false)

        // Obter os exercícios do currentTrainId.
        currentTrainId = arguments?.getInt("currentTrainId", -1) ?: -1

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvTasks: RecyclerView = view.findViewById(R.id.rv_exercise_list)
        rvTasks.adapter = exerciseAdapter

        exerciseAdapter.setOnItemClickListener { exercise ->
            val videoId = exercise.youtubeVideoId
            if (!videoId.isNullOrEmpty()) {
                val editTextVideoId = requireActivity().findViewById<EditText>(R.id.edt_task_video_url)
                editTextVideoId?.setText(videoId)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        listFromDataBase()
    }

    private fun listFromDataBase() {
        //Observer
        val listObserver = Observer<List<Exercise>>{ listExercises ->
            exerciseAdapter.submitList(listExercises)
        }

        //Live data
        exerciseViewModel.exerciseListLiveData.observe(viewLifecycleOwner, listObserver)
    }

    private fun openExerciseListDetail(exercise: Exercise) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage("Deseja excluir este exercício?")
            .setPositiveButton("Sim") { dialog, _ ->
                // Executar ação de exclusão aqui
                exerciseViewModel.deleteExercise(exercise)
                dialog.dismiss()
            }
            .setNegativeButton("Não") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance(currentTrainId: Int) = ExerciseListFragment().apply {
            arguments = Bundle().apply {
                putInt("currentTrainId", currentTrainId)
            }
        }
    }
}