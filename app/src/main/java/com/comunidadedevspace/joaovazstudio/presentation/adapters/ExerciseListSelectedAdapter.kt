package com.comunidadedevspace.joaovazstudio.presentation.adapters

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Paint
import android.net.Uri
import android.preference.PreferenceManager
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

    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val checkedExercisesSet: MutableSet<String> = mutableSetOf()

    init {
        val checkedExercises = sharedPreferences.getStringSet("checkedExercises", mutableSetOf())
        if (checkedExercises != null) {
            checkedExercisesSet.addAll(checkedExercises)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseListSelectedViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_exercise_selected, parent, false)

        return ExerciseListSelectedViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseListSelectedViewHolder, position: Int) {
        val exercise = getItem(position)
        holder.bind(exercise, openExerciseDetailView)

        holder.checkbox.setOnCheckedChangeListener(null)

        holder.checkbox.isChecked = checkedExercisesSet.contains(exercise.title)

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkedExercisesSet.add(exercise.title)
                exercise.isCompleted = true
            } else {
                checkedExercisesSet.remove(exercise.title)
                exercise.isCompleted = false
            }

            sharedPreferences.edit().putStringSet("checkedExercises", checkedExercisesSet).apply()

            updateExerciseSlctdAppearance(holder, isChecked)
        }

        updateExerciseSlctdAppearance(holder, holder.checkbox.isChecked)
    }

    private var onItemClickListener: ((Exercise) -> Unit)? = null

    fun setOnItemClickListener(listener: (Exercise) -> Unit) {
        onItemClickListener = listener
    }

    private fun updateExerciseSlctdAppearance(holder: ExerciseListSelectedViewHolder, isSelected: Boolean) {
        if (isSelected) {
            holder.tvExerciseSlctdTitle.alpha = 0.5f
            holder.tvExerciseSlctdDesc.alpha = 0.5f
            holder.tvExerciseSlctdTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.tvExerciseSlctdDesc.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.checkbox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green_300))
            holder.ivVideoThumbnail.alpha = 0.5f
        } else {
            holder.tvExerciseSlctdTitle.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.tvExerciseSlctdDesc.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.tvExerciseSlctdTitle.alpha = 1f
            holder.tvExerciseSlctdDesc.alpha = 1f
            holder.tvExerciseSlctdTitle.paintFlags = holder.tvExerciseSlctdTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.tvExerciseSlctdDesc.paintFlags = holder.tvExerciseSlctdDesc.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.checkbox.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.checkbox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
            holder.ivVideoThumbnail.alpha = 1f
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

    val tvExerciseSlctdTitle: TextView = view.findViewById(R.id.tv_exercise_selected_title)
    val tvExerciseSlctdDesc: TextView = view.findViewById(R.id.tv_exercise_selected_description)
    val ivVideoThumbnail: ImageView = view.findViewById(R.id.iv_video_thumbnail)
    val checkbox: CheckBox = view.findViewById(R.id.checkbox_exercise)

    fun bind(
        exercise: Exercise,
        openTaskDetailView:(exercise: Exercise) -> Unit
    ) {
        tvExerciseSlctdTitle.text = exercise.title
        tvExerciseSlctdDesc.text = exercise.description
        checkbox.isChecked = exercise.isSelected

        val youtubeVideoId = exercise.youtubeVideoId
        if (!youtubeVideoId.isNullOrEmpty()) {
            // Carrega a miniatura do vídeo usando Glide
            val videoThumbnailUrl = "https://img.youtube.com/vi/$youtubeVideoId/0.jpg"
            Glide.with(view)
                .load(videoThumbnailUrl)
                .into(ivVideoThumbnail)

            ivVideoThumbnail.setOnClickListener {
                // Redireciona o usuário para o vídeo no YouTube
                val youtubeVideoUrl = "https://www.youtube.com/watch?v=$youtubeVideoId"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeVideoUrl))
                view.context.startActivity(intent)
            }
        }
    }
}