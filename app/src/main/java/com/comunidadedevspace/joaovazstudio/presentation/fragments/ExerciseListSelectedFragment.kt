package com.comunidadedevspace.joaovazstudio.presentation.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.comunidadedevspace.joaovazstudio.JoaoVazStudio
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.data.local.Exercise
import com.comunidadedevspace.joaovazstudio.data.local.Train
import com.comunidadedevspace.joaovazstudio.presentation.adapters.ExerciseListSelectedAdapter
import com.comunidadedevspace.joaovazstudio.presentation.view.ExerciseDetailActivity
import com.comunidadedevspace.joaovazstudio.presentation.viewmodel.ExerciseListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExerciseListSelectedFragment : Fragment() {

    private var currentUserId: Long = -1L
    private var currentTrainId: Int = -1

    private val sharedPreferencesKey = "selected_train_id"
    private lateinit var sharedPreferences: SharedPreferences

    // Adapter
    private val exerciseAdapter: ExerciseListSelectedAdapter by lazy {
        ExerciseListSelectedAdapter(requireContext(), ::openExerciseListDetail)
    }

    // ViewModel
    private val exerciseViewModel: ExerciseListViewModel by lazy {
        ExerciseListViewModel.create(requireActivity().application, currentTrainId)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        checkSelectedTrainAndUpdateContent()

        // Salve o ID do treino selecionado no estado
        outState.putInt("selectedTrainId", sharedPreferences.getInt(sharedPreferencesKey, -1))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Verifique se há um estado anteriormente salvo
        val selectedTrainId = savedInstanceState?.getInt("selectedTrainId", -1)

        if (selectedTrainId != null && selectedTrainId != -1) {
            // Carregue os exercícios para o treino selecionado
            viewLifecycleOwner.lifecycleScope.launch {
                val selectedTrain = withContext(Dispatchers.IO) {
                    val trainDao = (requireActivity().application as JoaoVazStudio).getAppDataBase().trainDao()
                    trainDao.getTrainById(selectedTrainId)
                }

                if (selectedTrain != null) {
                    loadExercisesForSelectedTrain(selectedTrain)
                    updateTrainContentVisibility(true)
                } else {
                    updateTrainContentVisibility(false)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exercise_list_selected, container, false)

        currentUserId = arguments?.getLong("currentUserId", -1L) ?: -1L
        currentTrainId = arguments?.getInt("currentTrainId", -1) ?: -1

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkSelectedTrainAndUpdateContent()

        val rvTasks: RecyclerView = view.findViewById(R.id.selected_train_exercises)
        rvTasks.adapter = exerciseAdapter

        val fabSelectTrain: FloatingActionButton = view.findViewById(R.id.fab_select_train)

        fabSelectTrain.setOnClickListener {
            showSelectTrainDialog()
        }

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
        // Observer
        val listObserver = Observer<List<Exercise>> { listExercises ->
            exerciseAdapter.submitList(listExercises)
        }

        // LiveData
        exerciseViewModel.exerciseListLiveData.observe(viewLifecycleOwner, listObserver)
    }

    private fun openExerciseListDetail(exercise: Exercise) {
        val intent = ExerciseDetailActivity.start(requireContext(), exercise, 0)
        requireActivity().startActivity(intent)
    }

    private fun showSelectTrainDialog() {
        val trainDao = (requireActivity().application as JoaoVazStudio).getAppDataBase().trainDao()

        // Verifique se há um treino selecionado no SharedPreferences
        val selectedTrainId = sharedPreferences.getInt(sharedPreferencesKey, -1)

        // Observe a lista de treinos do usuário atual que têm exercícios associados
        trainDao.getTrainsWithExercisesByUserId(currentUserId).observe(viewLifecycleOwner) { trains ->
            val trainTitles = trains.map { it.trainTitle }.toTypedArray()

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Selecionar Treino Existente")
                .setSingleChoiceItems(trainTitles, -1) { dialog, which ->
                    val selectedTrain = trains[which]

                    sharedPreferences.edit().putInt(sharedPreferencesKey, selectedTrain.id).apply()

                    loadExercisesForSelectedTrain(selectedTrain)
                    updateTrainContentVisibility(true)
                    dialog.dismiss()
                }
                .setNegativeButton("Cancelar", null)
                .setNeutralButton("Limpar") { _, _ ->
                    clearSelectedTrain()
                }

            // Cria o AlertDialog separadamente
            val alertDialog = builder.create()
            alertDialog.show()

            // Se houver um treino selecionado no SharedPreferences, selecione-o no diálogo
            if (selectedTrainId != -1) {
                val selectedTrainIndex = trains.indexOfFirst { it.id == selectedTrainId }
                if (selectedTrainIndex != -1) {
                    val dialogListView = alertDialog.listView
                    dialogListView.setItemChecked(selectedTrainIndex, true)
                }
            }
        }
    }

    private fun loadExercisesForSelectedTrain(selectedTrain: Train) {
        val exerciseListViewModel = ExerciseListViewModel.create(requireActivity().application, selectedTrain.id)

        exerciseListViewModel.exerciseListLiveData.observe(viewLifecycleOwner) { listExercises ->
            exerciseAdapter.submitList(listExercises)

            sharedPreferences.edit().putInt(sharedPreferencesKey, selectedTrain.id).apply()
        }
    }

    private fun clearSelectedTrain() {
        // Limpe o treino selecionado no SharedPreferences
        sharedPreferences.edit().remove(sharedPreferencesKey).apply()

        // Limpe a lista de exercícios
        exerciseAdapter.submitList(null)

        // Oculte a lista de exercícios e mostre o conteúdo selecionar treino
        updateTrainContentVisibility(false)
    }

    private fun updateTrainContentVisibility(isTrainSelected: Boolean) {
        val selectedTrainContent = view?.findViewById<LinearLayout>(R.id.selected_train_content)
        if (isTrainSelected) {
            selectedTrainContent?.visibility = View.GONE
        } else {
            selectedTrainContent?.visibility = View.VISIBLE
        }
    }

    private fun checkSelectedTrainAndUpdateContent() {
        val selectedTrainId = sharedPreferences.getInt(sharedPreferencesKey, -1)
        if (selectedTrainId != -1) {
            viewLifecycleOwner.lifecycleScope.launch {
                val selectedTrain = withContext(Dispatchers.IO) {
                    val trainDao = (requireActivity().application as JoaoVazStudio).getAppDataBase().trainDao()
                    trainDao.getTrainById(selectedTrainId)
                }
                if (selectedTrain != null) {
                    loadExercisesForSelectedTrain(selectedTrain)
                    updateTrainContentVisibility(true)
                } else {
                    updateTrainContentVisibility(false)
                }
            }
        } else {
            updateTrainContentVisibility(false)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(currentTrainId: Int, currentUserId: Long) = ExerciseListSelectedFragment().apply {
            arguments = Bundle().apply {
                putInt("currentTrainId", currentTrainId)
                putLong("currentUserId", currentUserId)
            }
        }
    }
}