package com.comunidadedevspace.joaovazstudio.presentation

import android.content.Context
import android.content.Intent
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
import com.comunidadedevspace.joaovazstudio.authentication.AuthenticationManager
import com.comunidadedevspace.joaovazstudio.data.Train
import com.google.android.material.snackbar.Snackbar

class TrainDetailActivity: AppCompatActivity() {

    private var train: Train? = null
    private lateinit var btnAddTask: Button

    private val viewModel: TrainDetailViewModel by viewModels{
        TrainDetailViewModel.getVMFactory(application)
    }

    companion object{
        private const val TRAIN_DETAIL_EXTRA = "train.extra.detail"

        fun start(context: Context, train: Train?): Intent {
            val intent = Intent(context, TrainDetailActivity::class.java)
                .apply {
                    putExtra(TRAIN_DETAIL_EXTRA, train)
                }
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train_detail)
        setSupportActionBar(findViewById(R.id.toolbar))

        // Recuperar task
        train = intent.getSerializableExtra(TRAIN_DETAIL_EXTRA) as Train?

        val edtTrainTitle = findViewById<EditText>(R.id.edt_train_title)
        val edtTrainDescription = findViewById<EditText>(R.id.edt_train_description)

        btnAddTask = findViewById<Button>(R.id.btn_save_train)

        if(train != null) {
            edtTrainTitle.setText(train!!.trainTitle)
            edtTrainDescription.setText(train!!.trainDescription)
        }

        btnAddTask.setOnClickListener{
            val title = edtTrainTitle.text.toString()
            val desc = edtTrainDescription.text.toString()

            if(title.isNotEmpty() && desc.isNotEmpty()){
                if (train == null) {
                    addOrUpdateTrain(0, title, desc, ActionType.CREATE)
                } else {
                    addOrUpdateTrain(train!!.id, title, desc, ActionType.UPDATE)
                }
            } else {
                showMessage(it, "Treino necessita Título e Descrição.")
            }
        }
    }

    private fun addOrUpdateTrain(
        id: Int,
        title:String,
        description:String,
        actionType: ActionType
    ){
        val userId = AuthenticationManager.getCurrentUserId()
        val train = Train(id, userId, title, description)
        performAction(train, actionType)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_exercise_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_task -> {

                if(train != null){
                    performAction(train!!, ActionType.DELETE)
                }else{
                    showMessage(btnAddTask, "Impossível excluir item não criado.")
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun performAction(train: Train, actionType: ActionType){
        val trainAction = TrainAction(train, actionType.name)
        viewModel.execute(trainAction)
        finish()
    }

    private fun showMessage(view: View, message:String){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }
}