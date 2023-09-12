package com.comunidadedevspace.joaovazstudio.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.data.Task

class ExerciseListAdapter(
    private val context: Context,
    private val openExerciseDetailView: (task: Task) -> Unit
) : ListAdapter<Task, TaskListViewHolder>(ExerciseListAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)

        return TaskListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task, openExerciseDetailView)

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            task.isSelected = isChecked
            updateTaskAppearance(holder, isChecked)
        }
        updateTaskAppearance(holder, task.isSelected)

        // Carregando a imagem da tarefa no ImageView
        if (task.image != null) {
            val bitmap = BitmapFactory.decodeByteArray(task.image, 0, task.image!!.size)
            holder.ivTaskImage.setImageBitmap(bitmap)
        } else {
            // Caso a imagem seja nula, você pode definir uma imagem padrão
            holder.ivTaskImage.setImageResource(R.drawable.no_img_placeholder)
        }
    }

    private fun updateTaskAppearance(holder: TaskListViewHolder, isSelected: Boolean) {
        if (isSelected) {
            // Altere a aparência quando o CheckBox estiver selecionado
            holder.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.selectedTextColor))
            holder.tvDesc.setTextColor(ContextCompat.getColor(context, R.color.selectedTextColor))
            holder.checkbox.setTextColor(ContextCompat.getColor(context, R.color.selectedTextColor))
            holder.checkbox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.selectedTextColor))

//            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.selectedBackgroundColor))

            holder.tvTitle.alpha = 0.5f
            holder.tvDesc.alpha = 0.5f
            holder.ivTaskImage.alpha = 0.5f
            holder.checkbox.alpha = 0.5f
        } else {
            // Restaure a aparência padrão quando o CheckBox NÃO estiver selecionado
            holder.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.defaultTextColor))
            holder.tvDesc.setTextColor(ContextCompat.getColor(context, R.color.defaultTextColor))
            holder.checkbox.setTextColor(ContextCompat.getColor(context, R.color.defaultTextColor))
            holder.checkbox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.defaultTextColor))

//            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.defaultBackgroundColor))

            holder.tvTitle.alpha = 1.0f
            holder.tvDesc.alpha = 1.0f
            holder.ivTaskImage.alpha = 1.0f
            holder.checkbox.alpha = 1.0f
        }
    }

    companion object : DiffUtil.ItemCallback<Task>(){

        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.description == newItem.description
        }
    }
}

class TaskListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    val tvTitle: TextView = view.findViewById(R.id.tv_task_title)
    val tvDesc: TextView = view.findViewById(R.id.tv_task_description)
    val ivTaskImage: ImageView = view.findViewById(R.id.iv_task_image)
    val checkbox: CheckBox = view.findViewById(R.id.checkbox_task)

    fun bind(
        task: Task,
        openTaskDetailView:(task: Task) -> Unit
    ) {
        tvTitle.text = task.title
        tvDesc.text = task.description
        checkbox.isChecked = task.isSelected

        view.setOnClickListener {
            openTaskDetailView.invoke(task)
        }
    }
}