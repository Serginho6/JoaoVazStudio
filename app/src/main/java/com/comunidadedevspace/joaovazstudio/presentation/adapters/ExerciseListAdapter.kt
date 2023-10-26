package com.comunidadedevspace.joaovazstudio.presentation.adapters

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.data.models.Exercise

class ExerciseListAdapter(

    private var exerciseList: List<Exercise>,
    private val onAllItemsSelected: () -> Unit

) : RecyclerView.Adapter<ExerciseListAdapter.ExerciseListViewHolder>(){

    private val checkedExercisesSet: MutableSet<String> = mutableSetOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseListViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)

        return ExerciseListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseListViewHolder, position: Int) {
        val exercise = exerciseList[position]
        holder.bind(exercise)

        holder.checkbox.isChecked = checkedExercisesSet.contains(exercise.nome)

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkedExercisesSet.add(exercise.nome)
            } else {
                checkedExercisesSet.remove(exercise.nome)
            }

            holder.updateAppearance(holder.checkbox.isChecked)

            if (checkedExercisesSet.size == itemCount) {
                onAllItemsSelected()
            }
        }
        holder.updateAppearance(holder.checkbox.isChecked)
    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }

    class ExerciseListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val tvExerciseTitle: TextView = view.findViewById(R.id.tv_exercise_selected_title)
        private val tvExerciseDesc: TextView = view.findViewById(R.id.tv_exercise_selected_description)
        private val ivVideoThumbnail: ImageView = view.findViewById(R.id.iv_video_thumbnail)
        val checkbox: CheckBox = view.findViewById(R.id.checkbox_exercise)

        fun bind(exercise: Exercise) {
            tvExerciseTitle.text = exercise.nome
            tvExerciseDesc.text = exercise.desc

            val youtubeVideoId = exercise.youtube
            if (!youtubeVideoId.isNullOrEmpty()) {
                try {
                    val videoThumbnailUrl = "https://img.youtube.com/vi/$youtubeVideoId/0.jpg"
                    Glide.with(view)
                        .load(videoThumbnailUrl)
                        .into(ivVideoThumbnail)

                    ivVideoThumbnail.isClickable = true
                    ivVideoThumbnail.setOnClickListener {
                        val youtubeVideoUrl = "https://www.youtube.com/watch?v=$youtubeVideoId"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeVideoUrl))
                        view.context.startActivity(intent)
                    }
                } catch (e: Exception) {
                    ivVideoThumbnail.isClickable = false
                    ivVideoThumbnail.visibility = View.GONE
                }
            } else {
                ivVideoThumbnail.isClickable = false
                ivVideoThumbnail.visibility = View.GONE
            }
        }

        fun updateAppearance(isSelected: Boolean) {
            if (isSelected) {
                tvExerciseTitle.alpha = 0.5f
                tvExerciseDesc.alpha = 0.5f
                tvExerciseTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                tvExerciseDesc.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                checkbox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.sea_green))
                ivVideoThumbnail.alpha = 0.5f
            } else {
                tvExerciseTitle.setTextColor(ContextCompat.getColor(view.context, R.color.white))
                tvExerciseDesc.setTextColor(ContextCompat.getColor(view.context, R.color.white))
                tvExerciseTitle.alpha = 1f
                tvExerciseDesc.alpha = 1f
                tvExerciseTitle.paintFlags = tvExerciseTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                tvExerciseDesc.paintFlags = tvExerciseDesc.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                checkbox.setTextColor(ContextCompat.getColor(view.context, R.color.white))
                checkbox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.white))
                ivVideoThumbnail.alpha = 1f
            }
        }
    }
}