package com.comunidadedevspace.joaovazstudio.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.authentication.AuthenticationManager.getCurrentUserId
import com.comunidadedevspace.joaovazstudio.data.Train

class TrainListActivity : AppCompatActivity() {

    private lateinit var trainContent: LinearLayout

    // Adapter
    private val adapter: TrainListAdapter by lazy {
        TrainListAdapter(::openTrainListDetail)
    }

    private val viewModel: TrainListViewModel by lazy {
        TrainListViewModel.create(application, getCurrentUserId())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train_list)

        trainContent = findViewById(R.id.train_list_content)

        val rvTrains: RecyclerView = findViewById(R.id.rv_train_list)
        rvTrains.adapter = adapter

        val btnAddTrain: Button = findViewById(R.id.btn_add_train)

        btnAddTrain.setOnClickListener{
            val intent = Intent(this, TrainDetailActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        listFromDataBase()
    }

    private fun listFromDataBase() {
        // Observer
        val listObserver = Observer<List<Train>> { listTrains ->
            if (listTrains.isEmpty()) {
                trainContent.visibility = View.VISIBLE
            } else {
                trainContent.visibility = View.GONE
            }
            adapter.submitList(listTrains)
        }

        // LiveData
        viewModel.trainListLiveData.observe(this, listObserver)
    }

    private fun openTrainListDetail(train: Train) {
        val intent = TrainDetailActivity.start(this, train)
        startActivity(intent)
    }
}