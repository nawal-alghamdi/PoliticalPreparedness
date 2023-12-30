package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ElectionItemBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener) :
    ListAdapter<Election, ElectionListAdapter.ElectionViewHolder>(ElectionDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val election = getItem(position)
        holder.bind(election)
        holder.itemView.setOnClickListener {
            clickListener.onClick(election)
        }
    }

    // TODO: Create ElectionViewHolder
    class ElectionViewHolder(private var itemBinding: ElectionItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        // TODO: Bind ViewHolder
        fun bind(election: Election) {
            itemBinding.election = election
            itemBinding.executePendingBindings()
        }

        // TODO: Add companion object to inflate ViewHolder (from)
        companion object {
            fun from(parent: ViewGroup): ElectionViewHolder {
                val binding = ElectionItemBinding.inflate(
                    LayoutInflater.from(parent.context)
                )
                return ElectionViewHolder(binding)
            }
        }
    }


    // TODO: Create ElectionDiffCallback
    companion object ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {
        override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem.id == newItem.id
        }
    }

}

// TODO: Create ElectionListener
class ElectionListener(val clickListener: (election: Election) -> Unit) {
    fun onClick(election: Election) {
        clickListener(election)
    }
}