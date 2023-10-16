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
import com.comunidadedevspace.joaovazstudio.data.local.Train
import com.comunidadedevspace.joaovazstudio.presentation.adapters.TrainListAdapter
import com.comunidadedevspace.joaovazstudio.presentation.viewmodel.TrainListViewModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class TrainListActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userUid: String

    private lateinit var trainContent: LinearLayout
    private lateinit var btnAddTrain: Button

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

        // Usa o TrainListViewModel para obter a consulta aos Treinos cadastrados
        val trainListViewModel = ViewModelProvider(this).get(TrainListViewModel::class.java)
        val query = trainListViewModel.getTrainListQuery(userUid)

        // Configura as opções do FirestoreRecyclerAdapter
        val options = FirestoreRecyclerOptions.Builder<Train>()
            .setQuery(query, Train::class.java)
            .setLifecycleOwner(this)
            .build()

        // Inicializar o FirestoreRecyclerAdapter
        trainAdapter = TrainListAdapter(options) { train -> openTrainListDetail(train) }
        recyclerView.adapter = trainAdapter

        btnAddTrain = findViewById(R.id.btn_add_train)

        btnAddTrain.setOnClickListener {
            val intent = TrainDetailActivity.start(this, null, userUid)
            startActivity(intent)
        }
    }

    private fun openTrainListDetail(train: Train) {
        val intent = TrainDetailActivity.start(this, train, userUid)
        intent.putExtra("currentTrainId", train.id)
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
