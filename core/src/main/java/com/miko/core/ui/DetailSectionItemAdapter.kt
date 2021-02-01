package com.miko.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.miko.core.databinding.ItemChildBinding
import com.miko.core.domain.model.SectionItem

class DetailSectionItemAdapter(private val listItem: List<SectionItem>) :
    RecyclerView.Adapter<DetailSectionItemAdapter.ListViewHolder>() {
    inner class ListViewHolder(private val binding: ItemChildBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SectionItem) {
            binding.run {
                tvDetailString.text = item.name
            }
        }
    }

    private var binding: ItemChildBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        binding = ItemChildBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding as ItemChildBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listItem[position])
    }

    override fun getItemCount(): Int = listItem.size

    fun destroy() {
        binding = null
    }
}