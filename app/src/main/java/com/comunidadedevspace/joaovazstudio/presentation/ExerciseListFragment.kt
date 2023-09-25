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
import com.comunidadedevspace.joaovazstudio.data.Task

class ExerciseListFragment : Fragment() {

    private lateinit var ctnContent: LinearLayout

    //Adapter
    private val adapter: ExerciseListAdapter by lazy {
        ExerciseListAdapter(requireContext(), ::openTaskListDetail)
    }

    private val viewModel: ExerciseListViewModel by lazy {
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
        ctnContent = view.findViewById(R.id.selected_train_content)

        val rvTasks: RecyclerView = view.findViewById(R.id.rv_task_list)
        rvTasks.adapter = adapter

        adapter.setOnItemClickListener { task ->
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
        val listObserver = Observer<List<Task>>{ listTasks ->
            if(listTasks.isEmpty()){
                ctnContent.visibility = View.VISIBLE
            } else {
                ctnContent.visibility = View.GONE
            }
            adapter.submitList(listTasks)
        }

        //Live data
        viewModel.taskListLiveData.observe(this, listObserver)
    }

    private fun openTaskListDetail(task: Task) {
        val intent = ExerciseDetailActivity.start(requireContext(), task)
        requireActivity().startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ExerciseListFragment()
    }
}