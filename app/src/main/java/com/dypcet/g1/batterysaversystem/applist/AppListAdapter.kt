package com.dypcet.g1.batterysaversystem.applist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dypcet.g1.batterysaversystem.databinding.ListItemInstalledappBinding
import com.dypcet.g1.batterysaversystem.models.InstalledApp

class AppListAdapter : ListAdapter<InstalledApp, AppListAdapter.ViewHolder>(AppListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(private val binding: ListItemInstalledappBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemInstalledappBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(item: InstalledApp) {
            binding.app = item
            binding.executePendingBindings()
        }
    }

}

class AppListDiffCallback : DiffUtil.ItemCallback<InstalledApp>() {
    override fun areItemsTheSame(oldItem: InstalledApp, newItem: InstalledApp): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: InstalledApp, newItem: InstalledApp): Boolean {
        return oldItem == newItem
    }

}
