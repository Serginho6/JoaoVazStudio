package com.devserginho.joaovazstudio.presentation.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devserginho.joaovazstudio.R
import com.devserginho.joaovazstudio.presentation.adapters.TrainListAdapter
import com.devserginho.joaovazstudio.presentation.viewmodel.TrainListViewModel
import kotlinx.coroutines.launch

class TrainListActivity : AppCompatActivity() {

    private lateinit var userUid: String
    private lateinit var trainId: String

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var btnBackMain: Button

    private lateinit var recyclerView: RecyclerView

    private var trainAdapter: TrainListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train_list)

        sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        userUid = sharedPreferences.getString("userUid", null) ?: ""

        recyclerView = findViewById(R.id.rv_train_list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val trainListViewModel = ViewModelProvider(this).get(TrainListViewModel::class.java)

        trainAdapter = TrainListAdapter(emptyList()) { train ->
            trainId = train.nome
            openExerciseList(trainId)
        }

        recyclerView.adapter = trainAdapter

        lifecycleScope.launch {
            val trainList = trainListViewModel.getTrainList(userUid)
            trainAdapter?.updateTrainData(trainList)
        }

        btnBackMain = findViewById(R.id.btn_back_trains)

        btnBackMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openExerciseList(trainId: String) {
        val intent = ExerciseListActivity.start(this, trainId, userUid)
        startActivity(intent)
    }
}