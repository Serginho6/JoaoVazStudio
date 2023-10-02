package com.comunidadedevspace.joaovazstudio.presentation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.data.Train
import com.google.android.material.snackbar.Snackbar

class TrainDetailActivity: AppCompatActivity() {

    private var currentTrainId: Int = -1

    private lateinit var sharedPreferences: SharedPreferences

    private var train: Train? = null
    private lateinit var btnSaveTrain: Button

    private val trainDetailViewModel: TrainDetailViewModel by viewModels{
        TrainDetailViewModel.getVMFactory(application)
    }

    companion object{
        private const val TRAIN_DETAIL_EXTRA = "train.extra.detail"
        private const val CURRENT_TRAIN_ID_EXTRA = "currentTrainId.extra"

        fun start(context: Context, train: Train?, currentUserId: Long): Intent {
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

        val btnAddExercise = findViewById<Button>(R.id.add_exercise)

        btnAddExercise.setOnClickListener {
            if (train != null) {
                // Um treino já existe, permita adicionar um exercício.
                openExerciseDetail()
            } else {
                // Nenhum treino existe, exiba um Toast ou mensagem de alerta.
                showMessage(btnAddExercise, "Você precisa salvar a ficha antes de adicionar um exercício.")
            }
        }

        currentTrainId = intent.getIntExtra("currentTrainId", -1)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val exerciseListFragment = ExerciseListFragment.newInstance(currentTrainId)
        fragmentTransaction.replace(R.id.fragment_exercises_container, exerciseListFragment)
        fragmentTransaction.commit()

        // Recuperar treino
        train = intent.getSerializableExtra(TRAIN_DETAIL_EXTRA) as Train?

        val edtTrainTitle = findViewById<EditText>(R.id.edt_train_title)
        val edtTrainDescription = findViewById<EditText>(R.id.edt_train_description)

        btnSaveTrain = findViewById<Button>(R.id.btn_save_train)

        if(train != null) {
            edtTrainTitle.setText(train!!.trainTitle)
            edtTrainDescription.setText(train!!.trainDescription)
        }

        sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

        btnSaveTrain.setOnClickListener {
            val title = edtTrainTitle.text.toString()
            val desc = edtTrainDescription.text.toString()

            if (title.isNotEmpty() && desc.isNotEmpty()) {
                val userId = sharedPreferences.getLong("userId", -1) // Obtenha o userId do SharedPreferences
                if (userId != -1L) {
                    if (train == null) {
                        addOrUpdateTrain(0, userId, title, desc, ActionType.CREATE)
                    } else {
                        addOrUpdateTrain(train!!.id, userId, title, desc, ActionType.UPDATE)
                    }
                } else {
                    showMessage(btnSaveTrain, "Usuário não autenticado")
                }
            } else {
                showMessage(btnSaveTrain, "Treino necessita Título e Descrição.")
            }
        }
    }

    private fun addOrUpdateTrain(
        id: Int,
        userId: Long,
        title:String,
        description:String,
        actionType: ActionType
    ){
        val train = Train(id, userId, title, description)
        performAction(train, actionType)
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
                    showMessage(btnSaveTrain, "Impossível excluir item não criado.")
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
            // O usuário escolheu "Sim", agora você pode realizar a ação de exclusão.
            performAction(train!!, ActionType.DELETE)
        }
        builder.setNegativeButton("Não") { dialog, which ->
            // O usuário escolheu "Não", nada acontece.
            dialog.dismiss() // Fechar o diálogo
        }
        builder.show()
    }

    private fun performAction(train: Train, actionType: ActionType){
        val trainAction = TrainAction(train, actionType.name)
        trainDetailViewModel.execute(trainAction)
        finish()
    }

    private fun showMessage(view: View, message:String){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }

    private fun openExerciseDetail() {
        val trainId = train?.id ?: -1
        val intent = ExerciseDetailActivity.start(this, null, trainId)
        startActivity(intent)
    }
}