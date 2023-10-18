//package com.comunidadedevspace.joaovazstudio.presentation.adapters
//
//import android.content.Context
//import android.content.Intent
//import android.content.SharedPreferences
//import android.content.res.ColorStateList
//import android.graphics.Paint
//import android.net.Uri
//import android.preference.PreferenceManager
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.CheckBox
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.comunidadedevspace.joaovazstudio.R
//import com.comunidadedevspace.joaovazstudio.data.entities.Exercise
//
//class ExerciseListSelectedAdapter(
//    context: Context,
//    private val updateExerciseSelectionInDatabase: (exercise: Exercise, isSelected: Boolean) -> Unit,
//
//) : ListAdapter<Exercise, ExerciseListSelectedViewHolder>(ExerciseListAdapter) {
//
//    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//    private val checkedExercisesSet: MutableSet<String> = mutableSetOf()
//
//    init {
//        val checkedExercises = sharedPreferences.getStringSet("checkedExercises", mutableSetOf())
//        if (checkedExercises != null) {
//            checkedExercisesSet.addAll(checkedExercises)
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseListSelectedViewHolder {
//        val view: View = LayoutInflater
//            .from(parent.context)
//            .inflate(R.layout.item_exercise_selected, parent, false)
//
//        return ExerciseListSelectedViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ExerciseListSelectedViewHolder, position: Int) {
//        val exercise = getItem(position)
//        holder.bind(exercise)
//
//        holder.checkbox.isChecked = checkedExercisesSet.contains(exercise.title)
//
//        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                checkedExercisesSet.add(exercise.title)
//            } else {
//                checkedExercisesSet.remove(exercise.title)
//            }
//
//            sharedPreferences.edit().putStringSet("checkedExercises", checkedExercisesSet).apply()
//            holder.updateAppearance(holder.checkbox.isChecked)
//        }
//        holder.updateAppearance(holder.checkbox.isChecked)
//    }
//
//    private var onItemClickListener: ((Exercise) -> Unit)? = null
//
//    fun setOnItemClickListener(listener: (Exercise) -> Unit) {
//        onItemClickListener = listener
//    }
//
//    companion object : DiffUtil.ItemCallback<Exercise>(){
//
//        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
//            return oldItem == newItem
//        }
//
//        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
//            return oldItem.title == newItem.title &&
//                    oldItem.description == newItem.description
//        }
//    }
//}
//
//class ExerciseListSelectedViewHolder(
//    private val view: View,
//) : RecyclerView.ViewHolder(view) {
//
//    private val tvExerciseSlctdTitle: TextView = view.findViewById(R.id.tv_exercise_selected_title)
//    private val tvExerciseSlctdDesc: TextView = view.findViewById(R.id.tv_exercise_selected_description)
//    private val ivVideoThumbnail: ImageView = view.findViewById(R.id.iv_video_thumbnail)
//    val checkbox: CheckBox = view.findViewById(R.id.checkbox_exercise)
//
//    fun bind(
//        exercise: Exercise,
//    ) {
//        tvExerciseSlctdTitle.text = exercise.title
//        tvExerciseSlctdDesc.text = exercise.description
//        checkbox.isChecked = exercise.isSelected
//
//        val youtubeVideoId = exercise.youtubeVideoId
//        if (!youtubeVideoId.isNullOrEmpty()) {
//            // Carrega a miniatura do vídeo usando Glide dentro de um bloco try-catch
//            try {
//                val videoThumbnailUrl = "https://img.youtube.com/vi/$youtubeVideoId/0.jpg"
//                Glide.with(view)
//                    .load(videoThumbnailUrl)
//                    .into(ivVideoThumbnail)
//
//                ivVideoThumbnail.isClickable = true
//                ivVideoThumbnail.setOnClickListener {
//                    val youtubeVideoUrl = "https://www.youtube.com/watch?v=$youtubeVideoId"
//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeVideoUrl))
//                    view.context.startActivity(intent)
//                }
//            } catch (e: Exception) {
//                ivVideoThumbnail.isClickable = false
//                ivVideoThumbnail.visibility = View.GONE
//            }
//        } else {
//            ivVideoThumbnail.isClickable = false
//            ivVideoThumbnail.visibility = View.GONE
//        }
//    }
//
//    fun updateAppearance(isSelected: Boolean) {
//        if (isSelected) {
//            tvExerciseSlctdTitle.alpha = 0.5f
//            tvExerciseSlctdDesc.alpha = 0.5f
//            tvExerciseSlctdTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
//            tvExerciseSlctdDesc.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
//            checkbox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.sea_green))
//            ivVideoThumbnail.alpha = 0.5f
//        } else {
//            tvExerciseSlctdTitle.setTextColor(ContextCompat.getColor(view.context, R.color.white))
//            tvExerciseSlctdDesc.setTextColor(ContextCompat.getColor(view.context, R.color.white))
//            tvExerciseSlctdTitle.alpha = 1f
//            tvExerciseSlctdDesc.alpha = 1f
//            tvExerciseSlctdTitle.paintFlags = tvExerciseSlctdTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
//            tvExerciseSlctdDesc.paintFlags = tvExerciseSlctdDesc.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
//            checkbox.setTextColor(ContextCompat.getColor(view.context, R.color.white))
//            checkbox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, R.color.white))
//            ivVideoThumbnail.alpha = 1f
//        }
//    }
//}