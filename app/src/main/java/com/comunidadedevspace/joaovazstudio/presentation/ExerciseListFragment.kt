package com.comunidadedevspace.joaovazstudio.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.authentication.AuthenticationManager.getCurrentUserId
import com.comunidadedevspace.joaovazstudio.data.Exercise

class ExerciseListFragment : Fragment() {

    private lateinit var selectedTrainContent: LinearLayout

    //Adapter
    private val exerciseAdapter: ExerciseListAdapter by lazy {
        ExerciseListAdapter(requireContext(), ::openExerciseListDetail)
    }

    private val exerciseViewModel: ExerciseListViewModel by lazy {
        ExerciseListViewModel.create(requireActivity().application, getCurrentUserId())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exercise_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvTasks: RecyclerView = view.findViewById(R.id.rv_exercise_list)
        rvTasks.adapter = exerciseAdapter

        exerciseAdapter.setOnItemClickListener { task ->
            val videoId = task.youtubeVideoId
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
        val listObserver = Observer<List<Exercise>>{ listTasks ->
            exerciseAdapter.submitList(listTasks)
        }

        //Live data
        exerciseViewModel.exerciseListLiveData.observe(this, listObserver)
    }

    private fun openExerciseListDetail(exercise: Exercise) {
        val intent = ExerciseDetailActivity.start(requireContext(), exercise, 0)
        requireActivity().startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ExerciseListFragment()
    }
}