package com.comunidadedevspace.joaovazstudio.presentation

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.data.Train

class TrainListActivity : AppCompatActivity() {

    private var currentUserId: Long = -1L
    private lateinit var trainContent: LinearLayout
    private lateinit var btnAddTrain: Button

    // Adapter
    private val trainAdapter: TrainListAdapter by lazy {
        TrainListAdapter(::openTrainListDetail)
    }

    private val trainListViewModel: TrainListViewModel by lazy {
        TrainListViewModel.create(application, currentUserId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train_list)

        currentUserId = intent.getLongExtra("currentUserId", -1L)

        trainContent = findViewById(R.id.train_list_content)
        val rvTasks: RecyclerView = findViewById(R.id.rv_train_list)
        rvTasks.adapter = trainAdapter

        listFromDataBase()

        btnAddTrain = findViewById(R.id.btn_add_train)

        btnAddTrain.setOnClickListener {
            val intent = TrainDetailActivity.start(this, null, currentUserId)
            startActivity(intent)
        }
    }

    private fun listFromDataBase() {
        // Observer
        val listObserver = Observer<List<Train>> { listTrains ->
            if (listTrains.isEmpty()) {
                trainContent.visibility = View.VISIBLE
            } else {
                trainContent.visibility = View.GONE
            }
            trainAdapter.submitList(listTrains)
        }

        // Live data
        trainListViewModel.trainListLiveData.observe(this, listObserver)
    }

    private fun openTrainListDetail(train: Train) {
        val intent = TrainDetailActivity.start(this, train, currentUserId)
        intent.putExtra("currentTrainId", train.id)
        startActivity(intent)
    }
}