package com.comunidadedevspace.joaovazstudio.presentation.adapters

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
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
import com.bumptech.glide.Glide
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.data.local.Exercise

class ExerciseListSelectedAdapter(
    private val context: Context,
    private val openExerciseDetailView: (exercise: Exercise) -> Unit
) : ListAdapter<Exercise, ExerciseListSelectedViewHolder>(ExerciseListAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseListSelectedViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_exercise_selected, parent, false)

        return ExerciseListSelectedViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseListSelectedViewHolder, position: Int) {
        val exercise = getItem(position)
        holder.bind(exercise, openExerciseDetailView)

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            exercise.isSelected = isChecked
            updateTaskAppearance(holder, isChecked)
        }
        updateTaskAppearance(holder, exercise.isSelected)
    }

    private var onItemClickListener: ((Exercise) -> Unit)? = null

    fun setOnItemClickListener(listener: (Exercise) -> Unit) {
        onItemClickListener = listener
    }

    private fun updateTaskAppearance(holder: ExerciseListSelectedViewHolder, isSelected: Boolean) {
        if (isSelected) {
            // Altere a aparência quando o CheckBox estiver selecionado
            holder.tvTaskTitle.setTextColor(ContextCompat.getColor(context, R.color.selectedTextColor))
            holder.tvTaskDesc.setTextColor(ContextCompat.getColor(context, R.color.selectedTextColor))
            holder.checkbox.setTextColor(ContextCompat.getColor(context, R.color.selectedTextColor))
            holder.checkbox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.selectedTextColor))
            holder.ivTaskVideoThumbnail.alpha = 0.5f
        } else {
            // Restaure a aparência padrão quando o CheckBox NÃO estiver selecionado
            holder.tvTaskTitle.setTextColor(ContextCompat.getColor(context, R.color.defaultTextColor))
            holder.tvTaskDesc.setTextColor(ContextCompat.getColor(context, R.color.defaultTextColor))
            holder.checkbox.setTextColor(ContextCompat.getColor(context, R.color.defaultTextColor))
            holder.checkbox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.defaultTextColor))
            holder.ivTaskVideoThumbnail.alpha = 1f
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

class ExerciseListSelectedViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    val tvTaskTitle: TextView = view.findViewById(R.id.tv_task_title)
    val tvTaskDesc: TextView = view.findViewById(R.id.tv_task_description)
    val ivTaskVideoThumbnail: ImageView = view.findViewById(R.id.iv_video_thumbnail)
    val checkbox: CheckBox = view.findViewById(R.id.checkbox_task)

    fun bind(
        exercise: Exercise,
        openTaskDetailView:(exercise: Exercise) -> Unit
    ) {
        tvTaskTitle.text = exercise.title
        tvTaskDesc.text = exercise.description
        checkbox.isChecked = exercise.isSelected

        val youtubeVideoId = exercise.youtubeVideoId
        if (!youtubeVideoId.isNullOrEmpty()) {
            // Carregue a miniatura do vídeo usando Glide
            val videoThumbnailUrl = "https://img.youtube.com/vi/$youtubeVideoId/0.jpg"
            Glide.with(view)
                .load(videoThumbnailUrl)
                .into(ivTaskVideoThumbnail)

            ivTaskVideoThumbnail.setOnClickListener {
                // Redirecione o usuário para o vídeo no YouTube
                val youtubeVideoUrl = "https://www.youtube.com/watch?v=$youtubeVideoId"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeVideoUrl))
                view.context.startActivity(intent)
            }
        }
    }
}