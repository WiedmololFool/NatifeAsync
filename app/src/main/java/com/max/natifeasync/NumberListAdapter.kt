package com.max.natifeasync


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.max.natifeasync.databinding.ListNumberItemBinding

class NumberListAdapter : ListAdapter<Int, NumberListAdapter.NumberViewHolder>(NumberComparator()) {

    inner class NumberViewHolder(
        private val binding: ListNumberItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindItem(numberItem: Int) = with(binding) {
            tvNumber.text = numberItem.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NumberViewHolder(ListNumberItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bindItem(currentItem)
        }
    }

    class NumberComparator : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }

}