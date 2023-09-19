package com.comunidadedevspace.joaovazstudio.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.VideoView
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

        holder.itemView.setOnClickListener {
            openExerciseDetailView(task)
        }

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            task.isSelected = isChecked
            updateTaskAppearance(holder, isChecked)
        }
        updateTaskAppearance(holder, task.isSelected)
    }

    private var onItemClickListener: ((Task) -> Unit)? = null

    fun setOnItemClickListener(listener: (Task) -> Unit) {
        onItemClickListener = listener
    }

    private fun updateTaskAppearance(holder: TaskListViewHolder, isSelected: Boolean) {
        if (isSelected) {
            // Altere a aparência quando o CheckBox estiver selecionado
            holder.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.selectedTextColor))
            holder.tvDesc.setTextColor(ContextCompat.getColor(context, R.color.selectedTextColor))
            holder.checkbox.setTextColor(ContextCompat.getColor(context, R.color.selectedTextColor))
            holder.checkbox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.selectedTextColor))
        } else {
            // Restaure a aparência padrão quando o CheckBox NÃO estiver selecionado
            holder.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.defaultTextColor))
            holder.tvDesc.setTextColor(ContextCompat.getColor(context, R.color.defaultTextColor))
            holder.checkbox.setTextColor(ContextCompat.getColor(context, R.color.defaultTextColor))
            holder.checkbox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.defaultTextColor))
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
    val videoView: VideoView = view.findViewById(R.id.video_view_task)
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

        videoView.setOnClickListener {
            val youtubeVideoId = task.youtubeVideoId
            if (!youtubeVideoId.isNullOrEmpty()) {
                val videoUrl = "https://www.youtube.com/watch?v=$youtubeVideoId"
                playVideoInVideoView(videoView, videoUrl)
            }
        }
    }

    private fun playVideoInVideoView(videoView: VideoView, videoUrl: String) {
        val videoUri = Uri.parse(videoUrl)
        videoView.setVideoURI(videoUri)
        videoView.start()
    }
}