package com.comunidadedevspace.joaovazstudio.presentation.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.data.models.Exercise
import com.google.firebase.firestore.FirebaseFirestore

class ExerciseDetailActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    private var exercise: Exercise? = null

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var btnSaveExercise: Button

    companion object{
        private const val EXERCISE_DETAIL_EXTRA = "task.extra.detail"
        private const val TRAIN_ID_EXTRA = "train.extra.id"

        fun start(context: Context, exercise: Exercise?, trainId: String): Intent{
            val intent = Intent(context, ExerciseDetailActivity::class.java)
                .apply {
                    putExtra(EXERCISE_DETAIL_EXTRA, exercise)
                    putExtra(TRAIN_ID_EXTRA, trainId)
                }
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_detail)
        setSupportActionBar(findViewById(R.id.toolbar))

        // Recuperar exercício
        exercise = intent.getSerializableExtra(EXERCISE_DETAIL_EXTRA) as Exercise?

        // Recuperar ID do treino
        val trainId = intent.getStringExtra(TRAIN_ID_EXTRA)

        val edtTitle = findViewById<EditText>(R.id.edt_task_title)
        val edtDescription = findViewById<EditText>(R.id.edt_task_description)
        val edtVideoUrl = findViewById<EditText>(R.id.edt_task_video_url)

        btnSaveExercise = findViewById(R.id.btn_save_exercise)
        sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    }

    private fun extractVideoIdFromUrl(videoUrl: String): String {
        val uri = Uri.parse(videoUrl)
        val videoId = uri.getQueryParameter("v")

        return videoId ?: uri.lastPathSegment ?: ""
    }

    private fun isValidYouTubeVideoId(videoId: String): Boolean {
        return videoId.isNotEmpty() && videoId.matches(Regex("^[a-zA-Z0-9_-]{11}$"))
    }

    private fun showToast(message:String){
        Toast.makeText(this, message, Toast.LENGTH_LONG)
            .show()
    }
}