package com.devserginho.joaovazstudio.presentation.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devserginho.joaovazstudio.R
import com.devserginho.joaovazstudio.presentation.adapters.ExerciseListAdapter
import com.devserginho.joaovazstudio.presentation.viewmodel.ExerciseListViewModel
import kotlinx.coroutines.launch

class ExerciseListActivity : AppCompatActivity() {

    private lateinit var userUid: String
    private lateinit var selectedTrainId: String

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var btnBackTrain: Button

    private lateinit var recyclerView: RecyclerView

    private var allItemsCompleted = false

    private var exerciseAdapter: ExerciseListAdapter? = null

    companion object {
        private const val CURRENT_TRAIN_ID_EXTRA = "trainId.extra"
        private const val USER_UID_EXTRA = "UserUid.extra"

        fun start(context: Context, trainId: String, userUid: String): Intent {
            val intent = Intent(context, ExerciseListActivity::class.java)
            intent.putExtra(CURRENT_TRAIN_ID_EXTRA, trainId)
            intent.putExtra(USER_UID_EXTRA, userUid)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_list)

        if (savedInstanceState != null) {
            selectedTrainId = savedInstanceState.getString(CURRENT_TRAIN_ID_EXTRA, "")
            userUid = savedInstanceState.getString(USER_UID_EXTRA, "")
        }

        sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        userUid = sharedPreferences.getString("userUid", null) ?: ""

        selectedTrainId = intent.getStringExtra(CURRENT_TRAIN_ID_EXTRA) ?: ""
        Log.d("ExerciseListActivity", "Received trainId: $selectedTrainId")

        recyclerView = findViewById(R.id.rv_exercise_list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val exerciseListViewModel = ViewModelProvider(this).get(ExerciseListViewModel::class.java)

        lifecycleScope.launch {
            val exerciseList = exerciseListViewModel.getExerciseList(userUid, selectedTrainId)
            exerciseAdapter = ExerciseListAdapter(exerciseList) {
                allItemsCompleted = exerciseList.all { it.checked }
                showCompletedDialog()
            }
            recyclerView.adapter = exerciseAdapter
        }

        btnBackTrain = findViewById(R.id.btn_back_train)

        btnBackTrain.setOnClickListener {
            val intent = Intent(this, TrainListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showCompletedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Treino Concluído \uD83E\uDD75")
            .setMessage("\nUfa, o de hoje tá pago." + "\n\n\uD83C\uDF89 Parabéns e até o próximo!")
            .setPositiveButton("OK") { dialog, _ ->
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                dialog.dismiss()
            }
            .setCancelable(false)  // Impede que o diálogo seja fechado ao tocar fora dele
            .show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CURRENT_TRAIN_ID_EXTRA, selectedTrainId)
        outState.putString(USER_UID_EXTRA, userUid)
    }
}