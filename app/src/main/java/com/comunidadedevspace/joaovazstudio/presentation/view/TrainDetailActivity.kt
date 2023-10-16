package com.comunidadedevspace.joaovazstudio.presentation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.data.local.Train
import com.comunidadedevspace.joaovazstudio.presentation.fragments.ExerciseListFragment
import com.comunidadedevspace.joaovazstudio.presentation.viewmodel.TrainDetailViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TrainDetailActivity: AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    private var currentTrainId: Int = -1

    private var train: Train? = null
    private lateinit var btnSaveTrain: Button

    private val trainDetailViewModel: TrainDetailViewModel by viewModels{
        TrainDetailViewModel.getVMFactory(application)
    }

    companion object{
        private const val TRAIN_DETAIL_EXTRA = "train.extra.detail"
        private const val CURRENT_TRAIN_ID_EXTRA = "currentTrainId.extra"

        fun start(context: Context, train: Train?, currentUserId: String?): Intent {
            val intent = Intent(context, TrainDetailActivity::class.java)
            intent.putExtra(TRAIN_DETAIL_EXTRA, train)
            intent.putExtra(CURRENT_TRAIN_ID_EXTRA, currentUserId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train_detail)
        setSupportActionBar(findViewById(R.id.toolbar))

        currentTrainId = intent.getIntExtra("currentTrainId", -1)

        val btnAddExercise = findViewById<Button>(R.id.add_exercise)

//        btnAddExercise.setOnClickListener {
//            if (train != null) {
//                // Um treino já existe, permita adicionar um exercício.
//                openExerciseDetail()
//            } else {
//                // Nenhum treino existe, exiba um Toast ou mensagem de alerta.
//                showMessage(btnAddExercise, "Você precisa salvar a ficha antes de adicionar um exercício.")
//            }
//        }

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val exerciseListFragment = ExerciseListFragment.newInstance(currentTrainId)
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
                saveTrainToFirestore(userUid, title, desc)
            }
        }
    }

    private fun showMessage(view: View, message:String){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }

//    private fun openExerciseDetail() {
//        val trainId = train?.id ?: "-1"
//        val intent = ExerciseDetailActivity.start(this, null, trainId)
//        startActivity(intent)
//    }

    private fun saveTrainToFirestore(userUid: String, title: String, description: String) {
        if (userUid.isNotEmpty() && title.isNotEmpty() && description.isNotEmpty()) {
            val trainToSave = Train("", userUid, title, description)

            // Salve o treino na coleção "trains" do usuário
            val userTrainsCollection =
                db.collection("users")
                    .document(userUid)
                    .collection("trains")

            userTrainsCollection.add(trainToSave).addOnSuccessListener { documentReference ->
                showMessage(btnSaveTrain, "Treino salvo com sucesso.")

                // O ID do novo treino é documentReference.id
                val newTrainId = documentReference.id

                // Agora você pode salvar esse novo ID em qualquer lugar que precise.

                // Exemplo: Salve o ID no objeto train para referência futura.
                train?.id = newTrainId
            }.addOnFailureListener {
                showMessage(btnSaveTrain, "Falha ao salvar o treino.")
            }
        } else {
            showMessage(btnSaveTrain, "Treino deve ter título e descrição.")
        }
    }
}