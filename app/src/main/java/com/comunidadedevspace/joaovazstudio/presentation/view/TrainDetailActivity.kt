package com.comunidadedevspace.joaovazstudio.presentation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.data.models.Train
import com.comunidadedevspace.joaovazstudio.presentation.fragments.ExerciseListFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TrainDetailActivity: AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    private var userUid: String = ""
    private var trainId: String = ""

    private var train: Train? = null
    private lateinit var btnSaveTrain: Button

    companion object{
        private const val TRAIN_DETAIL_EXTRA = "train.extra.detail"
        private const val CURRENT_TRAIN_ID_EXTRA = "currentTrainId.extra"
        private const val USER_UID_EXTRA = "currentUserUid.extra"

        fun start(context: Context, train: Train?, trainId: String, userUid: String?): Intent {
            val intent = Intent(context, TrainDetailActivity::class.java)
            intent.putExtra(TRAIN_DETAIL_EXTRA, train)
            intent.putExtra(CURRENT_TRAIN_ID_EXTRA, trainId)
            intent.putExtra(USER_UID_EXTRA, userUid)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train_detail)
        setSupportActionBar(findViewById(R.id.toolbar))

        userUid = intent.getStringExtra(USER_UID_EXTRA).toString()
        trainId = intent.getStringExtra(CURRENT_TRAIN_ID_EXTRA).toString()

        val btnAddExercise = findViewById<Button>(R.id.add_exercise)

        btnAddExercise.setOnClickListener {
            if (train != null) {
                openExerciseDetail()
            } else {
                showToast("Você precisa salvar a ficha antes de adicionar um exercício.")
            }
        }

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val exerciseListFragment = ExerciseListFragment.newInstance(trainId)
        fragmentTransaction.replace(R.id.fragment_exercises_container, exerciseListFragment)
        fragmentTransaction.commit()

        // Recuperar treino
        train = intent.getSerializableExtra(TRAIN_DETAIL_EXTRA) as Train?

        val edtTrainTitle = findViewById<EditText>(R.id.edt_train_title)
        val edtTrainDescription = findViewById<EditText>(R.id.edt_train_description)

        btnSaveTrain = findViewById(R.id.btn_save_train)

        if(train != null) {
            edtTrainTitle.setText(train!!.trainTitle)
            edtTrainDescription.setText(train!!.trainDescription)
        }

        btnSaveTrain.setOnClickListener {
            val title = edtTrainTitle.text.toString()
            val desc = edtTrainDescription.text.toString()
            val userUid = FirebaseAuth.getInstance().currentUser?.uid

            if (userUid != null) {
                if (trainId.isEmpty()) {
                    createNewTrain(userUid, title, desc)
                } else {
                    updateTrain(userUid, trainId, title, desc)
                }
                val intent = Intent(this, TrainListActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun createNewTrain(userUid: String, title: String, description: String) {
        if (title.isNotEmpty() && description.isNotEmpty()) {
            val trainToSave = Train("", userUid, title, description) // Deixe o ID vazio para criar um novo treino

            val userTrainsCollection =
                db.collection("users")
                    .document(userUid)
                    .collection("trains")

            userTrainsCollection.add(trainToSave).addOnSuccessListener { documentReference ->
                showToast("Treino salvo com sucesso.")

                val newTrainId = documentReference.id

                trainId = newTrainId // Atualize o trainId com o ID gerado
            }.addOnFailureListener {
                showToast("Falha ao salvar o treino.")
            }
        } else {
            showToast("Treino deve ter título e descrição.")
        }
    }

    private fun updateTrain(userUid: String, trainId: String, title: String, description: String) {
        if (trainId.isNotEmpty() && title.isNotEmpty() && description.isNotEmpty()) {
            val trainToUpdate = Train(trainId, userUid, title, description)

            val userTrainsCollection =
                db.collection("users")
                    .document(userUid)
                    .collection("trains")

            userTrainsCollection.document(trainId).set(trainToUpdate)
                .addOnSuccessListener {
                    showToast("Treino atualizado com sucesso.")
                }
                .addOnFailureListener {
                    showToast("Falha ao atualizar o treino.")
                }
        } else {
            showToast("Treino deve ter título e descrição.")
        }
    }

    private fun deleteTrainFromFirestore(userUid: String, trainId: String) {
        if (userUid.isNotEmpty() && trainId.isNotEmpty()) {
            val userTrainsCollection = db.collection("users")
                .document(userUid)
                .collection("trains")

            userTrainsCollection.document(trainId).delete()
                .addOnSuccessListener {
                    showToast("Treino excluído com sucesso.")
                }
                .addOnFailureListener {
                    showToast("Falha ao excluir o treino.")
                }
        } else {
            showToast("Erro ao excluir o treino. ID do treino ou ID do usuário vazios.")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_train_or_exercise_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_task -> {

                if(train != null){
                    showConfirmationDialog()
                }else{
                    showToast("Impossível excluir item não criado.")
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmação")
        builder.setMessage("Tem certeza que deseja excluir TODO o treino?")
        builder.setPositiveButton("Sim") { dialog, which ->
            if (train != null) {
                deleteTrainFromFirestore(userUid, train!!.trainId)
                val intent = Intent(this, TrainListActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        builder.setNegativeButton("Não") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun openExerciseDetail() {
        val trainId = train?.trainId ?: ""
        val intent = ExerciseDetailActivity.start(this, null, trainId)
        startActivity(intent)
    }

    private fun showToast(message:String){
        Toast.makeText(this, message, Toast.LENGTH_LONG)
            .show()
    }
}