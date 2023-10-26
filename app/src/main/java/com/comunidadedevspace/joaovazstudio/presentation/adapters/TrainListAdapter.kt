package com.comunidadedevspace.joaovazstudio.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.data.models.Train

class TrainListAdapter(

    private var trainList: List<Train>,
    private val openTrainDetailView: (train: Train) -> Unit

) : RecyclerView.Adapter<TrainListAdapter.TrainListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainListViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_train, parent, false)

        return TrainListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainListViewHolder, position: Int) {
        val train = trainList[position]
        holder.bind(train, openTrainDetailView)
    }

    override fun getItemCount(): Int {
        return trainList.size
    }

    fun updateTrainData(newTrainList: List<Train>) {
        trainList = newTrainList
        notifyDataSetChanged()
    }

    class TrainListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val tvTrainTitle: TextView = view.findViewById(R.id.tv_train_title)
        private val tvTrainDesc: TextView = view.findViewById(R.id.tv_train_description)

        fun bind(train: Train, openTrainDetailView: (train: Train) -> Unit) {
            tvTrainTitle.text = train.nome
            tvTrainDesc.text = train.desc

            view.setOnClickListener {
                openTrainDetailView.invoke(train)
            }
        }
    }
}