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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ExerciseListAdapter(
    options: FirestoreRecyclerOptions<Exercise>
) : FirestoreRecyclerAdapter<Exercise, ExerciseListAdapter.ExerciseListViewHolder>(options){

//    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val checkedExercisesSet: MutableSet<String> = mutableSetOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseListViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)

        return ExerciseListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseListViewHolder, position: Int, model: Exercise) {
        holder.bind(model)

        holder.checkbox.isChecked = checkedExercisesSet.contains(model.title)

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkedExercisesSet.add(model.title)
            } else {
                checkedExercisesSet.remove(model.title)
            }

//            sharedPreferences.edit().putStringSet("checkedExercises", checkedExercisesSet).apply()
            holder.updateAppearance(holder.checkbox.isChecked)
        }
        holder.updateAppearance(holder.checkbox.isChecked)
    }

    class ExerciseListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val tvExerciseTitle: TextView = view.findViewById(R.id.tv_exercise_selected_title)
        private val tvExerciseDesc: TextView = view.findViewById(R.id.tv_exercise_selected_description)
        private val ivVideoThumbnail: ImageView = view.findViewById(R.id.iv_video_thumbnail)
        val checkbox: CheckBox = view.findViewById(R.id.checkbox_exercise)

        fun bind(exercise: Exercise) {
            tvExerciseTitle.text = exercise.title
            tvExerciseDesc.text = exercise.desc
            checkbox.isChecked = exercise.isSelected

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
