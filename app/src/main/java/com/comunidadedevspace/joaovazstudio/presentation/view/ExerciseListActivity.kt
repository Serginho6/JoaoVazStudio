package com.comunidadedevspace.joaovazstudio.presentation.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.data.models.Exercise
import com.comunidadedevspace.joaovazstudio.presentation.adapters.ExerciseListAdapter
import com.comunidadedevspace.joaovazstudio.presentation.viewmodel.ExerciseListViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ExerciseListActivity : AppCompatActivity() {

    private lateinit var userUid: String
    private lateinit var selectedTrainId: String

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var btnBackTrain: Button

    private lateinit var recyclerView: RecyclerView
    private lateinit var exerciseAdapter: ExerciseListAdapter

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

        sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        userUid = sharedPreferences.getString("userUid", null) ?: ""

        selectedTrainId = intent.getStringExtra(CURRENT_TRAIN_ID_EXTRA) ?: ""
        Log.d("ExerciseListActivity", "Received trainId: $selectedTrainId")

        recyclerView = findViewById(R.id.rv_exercise_list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val exerciseListViewModel = ViewModelProvider(this).get(ExerciseListViewModel::class.java)
        val query = exerciseListViewModel.getExerciseListQuery(userUid, selectedTrainId)

        val options = FirestoreRecyclerOptions.Builder<Exercise>()
            .setQuery(query, Exercise::class.java)
            .setLifecycleOwner(this)
            .build()

        exerciseAdapter = ExerciseListAdapter(options)
        recyclerView.adapter = exerciseAdapter

        btnBackTrain = findViewById(R.id.btn_back_train)

        btnBackTrain.setOnClickListener {
            val intent = Intent(this, TrainListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        exerciseAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        exerciseAdapter.stopListening()
    }
}
