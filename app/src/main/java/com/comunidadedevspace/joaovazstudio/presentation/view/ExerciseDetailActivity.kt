package com.comunidadedevspace.joaovazstudio.presentation.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.data.local.Exercise
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
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

        btnSaveExercise.setOnClickListener {
            val title = edtTitle.text.toString()
            val desc = edtDescription.text.toString()
            val videoUrl = edtVideoUrl.text.toString()

            val userUid = FirebaseAuth.getInstance().currentUser?.uid

            if (userUid != null) {
                val videoId = extractVideoIdFromUrl(videoUrl)

                // Verifica se o videoId é válido apenas se o campo de vídeo estiver preenchido
                if (videoUrl.isBlank() || isValidYouTubeVideoId(videoId)) {
                    if (title.isNotEmpty() && desc.isNotEmpty()) {
                        // Chama a função para salvar o exercício no Firestore.
                        if (trainId != null) {
                            saveExerciseToFirestore(userUid, trainId, title, desc, videoId)
                        }
                    } else {
                        showMessage(it, "O exercício deve ter um título válido.")
                    }
                } else {
                    showMessage(it, "URL do vídeo do YouTube inválida") // Exibe uma mensagem de erro se a URL for inválida
                }
            } else {
                showMessage(it, "Usuário não autenticado")
            }
        }
    }

    private fun saveExerciseToFirestore(userUid: String, trainId: String, title: String, description: String, videoId: String) {
        if (userUid.isNotEmpty() && trainId.isNotEmpty() && title.isNotEmpty()) {
            val exerciseToSave = Exercise("", userUid, trainId, title, description, videoId, isSelected = false)

            // Salve o exercício na coleção "exercises" do treino no Firestore.
            val trainExercisesCollection =
                db.collection("users")
                .document(userUid)
                .collection("trains")
                .document(trainId)
                .collection("exercises")

            trainExercisesCollection.add(exerciseToSave).addOnSuccessListener { documentReference ->
                showMessage(btnSaveExercise, "Exercício salvo com sucesso.")

                // O ID do novo exercício é documentReference.id
                val newExerciseId = documentReference.id

                // Exemplo: Salve o ID no objeto exercise para referência futura.
                exercise?.id = newExerciseId
            }.addOnFailureListener {
                showMessage(btnSaveExercise, "Falha ao salvar o exercício.")
            }
        } else {
            showMessage(btnSaveExercise, "O exercício deve ter um título válido.")
        }
    }

    private fun extractVideoIdFromUrl(videoUrl: String): String {
        // Tenta extrair o ID do vídeo a partir da URL no formato "https://www.youtube.com/watch?v=..."
        val uri = Uri.parse(videoUrl)
        val videoId = uri.getQueryParameter("v")

        // Se não foi possível extrair o ID a partir do formato acima, tenta extrair do formato "https://youtu.be/..."
        return videoId ?: uri.lastPathSegment ?: ""
    }

//    private fun performAction(exercise: Exercise, actionType: ActionType){
//        val exerciseAction = ExerciseAction(exercise, actionType.name)
//        viewModel.execute(exerciseAction)
//        finish()
//    }

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