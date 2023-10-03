package com.comunidadedevspace.joaovazstudio.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.data.local.Exercise

class ExerciseListAdapter(
    private val context: Context,
    private val openExerciseDetailView: (exercise: Exercise) -> Unit
) : ListAdapter<Exercise, ExerciseListViewHolder>(ExerciseListAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseListViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)

        return ExerciseListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseListViewHolder, position: Int) {
        val exercise = getItem(position)
        holder.bind(exercise, openExerciseDetailView)

        updateExerciseAppearance(holder, exercise.isSelected)

        holder.itemView.setOnClickListener {
            openExerciseDetailView(exercise)
        }
    }

    private var onItemClickListener: ((Exercise) -> Unit)? = null

    fun setOnItemClickListener(listener: (Exercise) -> Unit) {
        onItemClickListener = listener
    }

    private fun updateExerciseAppearance(holder: ExerciseListViewHolder, isSelected: Boolean) {
        if (isSelected) {
            // Altere a aparência quando o CheckBox estiver selecionado
            holder.tvExerciseTitle.setTextColor(ContextCompat.getColor(context, R.color.selectedTextColor))
            holder.tvExerciseDesc.setTextColor(ContextCompat.getColor(context, R.color.selectedTextColor))
        } else {
            // Restaure a aparência padrão quando o CheckBox NÃO estiver selecionado
            holder.tvExerciseTitle.setTextColor(ContextCompat.getColor(context, R.color.defaultTextColor))
            holder.tvExerciseDesc.setTextColor(ContextCompat.getColor(context, R.color.defaultTextColor))
        }
    }

    companion object : DiffUtil.ItemCallback<Exercise>(){

        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.description == newItem.description
        }
    }
}

class ExerciseListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    val tvExerciseTitle: TextView = view.findViewById(R.id.tv_task_title)
    val tvExerciseDesc: TextView = view.findViewById(R.id.tv_task_description)

    fun bind(
        exercise: Exercise,
        openTaskDetailView:(exercise: Exercise) -> Unit
    ) {
        tvExerciseTitle.text = exercise.title
        tvExerciseDesc.text = exercise.description

        view.setOnClickListener {
            openTaskDetailView.invoke(exercise)
        }
    }

    companion object : DiffUtil.ItemCallback<Exercise>(){

        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.description == newItem.description
        }
    }
}