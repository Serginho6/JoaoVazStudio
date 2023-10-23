package com.comunidadedevspace.joaovazstudio.presentation.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.data.models.Train
import com.comunidadedevspace.joaovazstudio.presentation.adapters.TrainListAdapter
import com.comunidadedevspace.joaovazstudio.presentation.viewmodel.TrainListViewModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class TrainListActivity : AppCompatActivity() {

    private lateinit var userUid: String
    private lateinit var trainId: String

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var trainContent: LinearLayout
    private lateinit var btnBackMain: Button

    private lateinit var recyclerView: RecyclerView
    private lateinit var trainAdapter: FirestoreRecyclerAdapter<Train, TrainListAdapter.TrainListViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train_list)

        sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        userUid = sharedPreferences.getString("userUid", null) ?: ""

        trainContent = findViewById(R.id.train_list_content)
        recyclerView = findViewById(R.id.rv_train_list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val trainListViewModel = ViewModelProvider(this).get(TrainListViewModel::class.java)
        val query = trainListViewModel.getTrainListQuery(userUid)

        val options = FirestoreRecyclerOptions.Builder<Train>()
            .setQuery(query, Train::class.java)
            .setLifecycleOwner(this)
            .build()

        trainAdapter = TrainListAdapter(options) { train ->
            trainId = train.trainId
            openExerciseList(trainId)
        }

        recyclerView.adapter = trainAdapter

        btnBackMain = findViewById(R.id.btn_back_trains)

        btnBackMain.setOnClickListener {
            finish()
        }
    }

    private fun openExerciseList(trainId: String) {
        val intent = ExerciseListActivity.start(this, trainId, userUid)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        trainAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        trainAdapter.stopListening()
    }
}