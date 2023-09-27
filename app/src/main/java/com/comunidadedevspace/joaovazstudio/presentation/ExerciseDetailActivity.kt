package com.comunidadedevspace.joaovazstudio.presentation

import android.content.Context
import android.content.Intent
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
import com.comunidadedevspace.joaovazstudio.authentication.AuthenticationManager.getCurrentUserId
import com.comunidadedevspace.joaovazstudio.data.Exercise
import com.google.android.material.snackbar.Snackbar

class ExerciseDetailActivity : AppCompatActivity() {

    private lateinit var editTextVideoUrl: EditText
    private var youtubeVideoUrl: String? = null

    private var exercise: Exercise? = null
    private lateinit var btnSaveExercise: Button

    private val viewModel: ExerciseDetailViewModel by viewModels{
        ExerciseDetailViewModel.getVMFactory(application)
    }

    companion object{
        private const val TASK_DETAIL_EXTRA = "task.extra.detail"
        private const val TRAIN_ID_EXTRA = "train.extra.id"

        fun start(context: Context, exercise: Exercise?, trainId: Int): Intent{
            val intent = Intent(context, ExerciseDetailActivity::class.java)
                .apply {
                    putExtra(TASK_DETAIL_EXTRA, exercise)
                    putExtra(TRAIN_ID_EXTRA, trainId)
                }
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_detail)
        setSupportActionBar(findViewById(R.id.toolbar))

        editTextVideoUrl = findViewById(R.id.edt_task_video_url)

        // Recuperar exercício
        exercise = intent.getSerializableExtra(TASK_DETAIL_EXTRA) as Exercise?

        // Recuperar ID do treino
        val trainId = intent.getIntExtra(TRAIN_ID_EXTRA, 0)

        val edtTitle = findViewById<EditText>(R.id.edt_task_title)
        val edtDescription = findViewById<EditText>(R.id.edt_task_description)
        val edtVideoUrl = findViewById<EditText>(R.id.edt_task_video_url)

        btnSaveExercise = findViewById<Button>(R.id.btn_save_exercise)

        if(exercise != null) {
            edtTitle.setText(exercise!!.title)
            edtDescription.setText(exercise!!.description)
            edtVideoUrl.setText(exercise!!.youtubeVideoId)
        }

        btnSaveExercise.setOnClickListener{
            val title = edtTitle.text.toString()
            val desc = edtDescription.text.toString()
            val videoUrl = edtVideoUrl.text.toString()

            if(title.isNotEmpty() && desc.isNotEmpty() && videoUrl.isNotEmpty()){
                val videoId = extractVideoIdFromUrl(videoUrl)
                if (videoId.isNotEmpty()) {
                    if (exercise == null) {
                        addOrUpdateExercise(0, trainId, title, desc, videoId, ActionType.CREATE)
                    } else {
                        addOrUpdateExercise(exercise!!.id, trainId, title, desc, videoId, ActionType.UPDATE)
                    }
                } else {
                    showMessage(it, "URL do vídeo inválida")
                }
            } else {
                showMessage(it, "É necessário adicionar Exercício, Quantidade e URL do vídeo")
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
        title: String,
        description: String,
        videoId: String,
        actionType: ActionType
    ){
        val userId = getCurrentUserId()
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
        val taskAction = TaskAction(exercise, actionType.name)
        viewModel.execute(taskAction)
        finish()
    }

    private fun showMessage(view: View, message:String){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }
}