package com.comunidadedevspace.joaovazstudio.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.data.models.Exercise
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ExerciseListAdapter(
    options: FirestoreRecyclerOptions<Exercise>,
    private val showDeleteExerciseDialog: (exercise: Exercise) -> Unit,
    private val deleteExerciseCallback: (exercise: Exercise) -> Unit
) : FirestoreRecyclerAdapter<Exercise, ExerciseListAdapter.ExerciseListViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseListViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)

        return ExerciseListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseListViewHolder, position: Int, model: Exercise) {
        holder.bind(model, showDeleteExerciseDialog)
    }

    class ExerciseListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val tvExerciseTitle: TextView = view.findViewById(R.id.tv_task_title)
        private val tvExerciseDesc: TextView = view.findViewById(R.id.tv_task_description)

        fun bind(exercise: Exercise, showDeleteExerciseDialog: (exercise: Exercise) -> Unit) {
            tvExerciseTitle.text = exercise.title
            tvExerciseDesc.text = exercise.description

            view.setOnClickListener {
                showDeleteExerciseDialog.invoke(exercise)
            }
        }
    }

    fun deleteExercise(exercise: Exercise) {
        deleteExerciseCallback(exercise)
    }
}