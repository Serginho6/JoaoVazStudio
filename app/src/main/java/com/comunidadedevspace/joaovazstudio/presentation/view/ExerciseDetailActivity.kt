package com.comunidadedevspace.joaovazstudio.presentation.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.data.database.ActionType
import com.comunidadedevspace.joaovazstudio.data.database.ExerciseAction
import com.comunidadedevspace.joaovazstudio.data.local.Exercise
import com.comunidadedevspace.joaovazstudio.presentation.viewmodel.ExerciseDetailViewModel
import com.google.android.material.snackbar.Snackbar

class ExerciseDetailActivity : AppCompatActivity() {

    private var exercise: Exercise? = null

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var btnSaveExercise: Button

    private val viewModel: ExerciseDetailViewModel by viewModels{
        ExerciseDetailViewModel.getVMFactory(application)
    }

    companion object{
        private const val EXERCISE_DETAIL_EXTRA = "task.extra.detail"
        private const val TRAIN_ID_EXTRA = "train.extra.id"

        fun start(context: Context, exercise: Exercise?, trainId: Int): Intent{
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
        val trainId = intent.getIntExtra(TRAIN_ID_EXTRA, 0)

        val edtTitle = findViewById<EditText>(R.id.edt_task_title)
        val edtDescription = findViewById<EditText>(R.id.edt_task_description)
        val edtVideoUrl = findViewById<EditText>(R.id.edt_task_video_url)

        btnSaveExercise = findViewById(R.id.btn_save_exercise)

        if(exercise != null) {
            edtTitle.setText(exercise!!.title)
            edtDescription.setText(exercise!!.description)
            edtVideoUrl.setText(exercise!!.youtubeVideoId)
        }

        sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

        btnSaveExercise.setOnClickListener {
            val title = edtTitle.text.toString()
            val desc = edtDescription.text.toString()
            val videoUrl = edtVideoUrl.text.toString()

            val userId = sharedPreferences.getLong("userId", -1L)

            if (userId != -1L) {
                val videoId = extractVideoIdFromUrl(videoUrl)

                // Verifica se o videoId é válido apenas se o campo de vídeo estiver preenchido
                if (videoUrl.isBlank() || isValidYouTubeVideoId(videoId)) {
                    if (title.isNotEmpty() && desc.isNotEmpty()) {
                        if (exercise == null) {
                            addOrUpdateExercise(0, trainId, userId, title, desc, videoId, ActionType.CREATE)
                        } else {
                            addOrUpdateExercise(exercise!!.id, trainId, userId, title, desc, videoId, ActionType.UPDATE)
                        }
                    } else {
                        showMessage(it, "É necessário adicionar Exercício e Quantidade")
                    }
                } else {
                    showMessage(it, "URL do vídeo do YouTube inválida") // Exibe uma mensagem de erro se a URL for inválida
                }
            } else {
                showMessage(it, "Usuário não autenticado")
            }
        }

    }

    private fun extractVideoIdFromUrl(videoUrl: String): String {
        // Tenta extrair o ID do vídeo a partir da URL no formato "https://www.youtube.com/watch?v=..."
        val uri = Uri.parse(videoUrl)
        val videoId = uri.getQueryParameter("v")

        // Se não foi possível extrair o ID a partir do formato acima, tenta extrair do formato "https://youtu.be/..."
        return videoId ?: uri.lastPathSegment ?: ""
    }

    private fun addOrUpdateExercise(
        id: Int,
        trainId: Int,
        userId: Long,
        title: String,
        description: String,
        videoId: String,
        actionType: ActionType
    ){
        val exercise = Exercise(id, userId, trainId, title, description, videoId, isSelected = false)
        performAction(exercise, actionType)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_train_or_exercise_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_task -> {

                if(exercise != null){
                    performAction(exercise!!, ActionType.DELETE)
                }else{
                    showMessage(btnSaveExercise, "Impossível excluir item não criado.")
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun performAction(exercise: Exercise, actionType: ActionType){
        val exerciseAction = ExerciseAction(exercise, actionType.name)
        viewModel.execute(exerciseAction)
        finish()
    }

    private fun showMessage(view: View, message:String){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }

    private fun isValidYouTubeVideoId(videoId: String): Boolean {
        // Verifica se o videoId é não nulo e possui o formato correto de um ID do YouTube
        return videoId.isNotEmpty() && videoId.matches(Regex("^[a-zA-Z0-9_-]{11}$"))
    }
}