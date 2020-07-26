package com.example.githubbrowser.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.githubbrowser.R

class RepoShaAdapter : RecyclerView.Adapter<RepoShaAdapter.ShaViewHolder>() {
    private var data: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.support_simple_spinner_dropdown_item, parent, false)
        return ShaViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ShaViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun update(newData: List<String>) {
        val diffCallback = StringDiffCallback(data, newData)
        val diff = DiffUtil.calculateDiff(diffCallback)
        data = newData
        diff.dispatchUpdatesTo(this)
    }

    class ShaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(string: String) {
            (itemView as TextView).text = string
        }
    }
}

private class StringDiffCallback(
    private val newData: List<String>,
    private val oldData: List<String>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areContentsTheSame(oldItemPosition, newItemPosition)
    }

    override fun getOldListSize() = oldData.size

    override fun getNewListSize() = newData.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return  newData[newItemPosition] == oldData[oldItemPosition]
    }

}