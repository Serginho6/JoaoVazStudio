package com.comunidadedevspace.joaovazstudio.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.comunidadedevspace.joaovazstudio.R
import com.comunidadedevspace.joaovazstudio.data.local.Train

class TrainListAdapter(
    private val openTrainDetailView: (train: Train) -> Unit
) : ListAdapter<Train, TrainListViewHolder>(TrainListAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainListViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_train, parent, false)

        return TrainListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainListViewHolder, position: Int) {
        val train = getItem(position)
        holder.bind(train, openTrainDetailView)
        holder.itemView
    }

    companion object : DiffUtil.ItemCallback<Train>(){

        override fun areItemsTheSame(oldItem: Train, newItem: Train): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Train, newItem: Train): Boolean {
            return oldItem.trainTitle == newItem.trainTitle &&
                    oldItem.trainDescription == newItem.trainDescription
        }
    }

    fun submitTrainList(trainList: List<Train>) {
        submitList(trainList)
    }
}
class TrainListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val tvTrainTitle: TextView = view.findViewById(R.id.tv_train_title)
    private val tvTrainDesc: TextView = view.findViewById(R.id.tv_train_description)

    fun bind(
        train: Train,
        openTrainDetailView:(train: Train) -> Unit
    ) {
        tvTrainTitle.text = train.trainTitle
        tvTrainDesc.text = train.trainDescription

        view.setOnClickListener{
            openTrainDetailView.invoke(train)
        }
    }
}