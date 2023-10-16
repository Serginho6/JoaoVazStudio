package com.comunidadedevspace.joaovazstudio.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.data.local.Train
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class TrainListAdapter(
    options: FirestoreRecyclerOptions<Train>,
    private val openTrainDetailView: (train: Train) -> Unit
) : FirestoreRecyclerAdapter<Train, TrainListAdapter.TrainListViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainListViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_train, parent, false)

        return TrainListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainListViewHolder, position: Int, model: Train) {
        holder.bind(model, openTrainDetailView)
    }

    class TrainListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val tvTrainTitle: TextView = view.findViewById(R.id.tv_train_title)
        private val tvTrainDesc: TextView = view.findViewById(R.id.tv_train_description)

        fun bind(train: Train, openTrainDetailView: (train: Train) -> Unit) {
            tvTrainTitle.text = train.trainTitle
            tvTrainDesc.text = train.trainDescription

            view.setOnClickListener {
                openTrainDetailView.invoke(train)
            }
        }
    }
}