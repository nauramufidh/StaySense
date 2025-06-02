package com.example.staysense.ui.profile.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.staysense.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(
    private val onItemClick: (CombinedHistory) -> Unit
) : ListAdapter<CombinedHistory, HistoryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvSource: TextView = itemView.findViewById(R.id.tvSource)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)

        fun bind(item: CombinedHistory) {
            tvSource.text = item.source
            tvDate.text = formatTimestamp(item.timestamp)

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    private fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CombinedHistory>() {
            override fun areItemsTheSame(oldItem: CombinedHistory, newItem: CombinedHistory): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CombinedHistory, newItem: CombinedHistory): Boolean =
                oldItem == newItem
        }
    }
}