package com.miko.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.miko.core.R
import com.miko.core.databinding.ListRowSearchBinding
import com.miko.core.domain.model.GameList

class SearchAdapter(private val gameLists: ArrayList<GameList> = ArrayList()) :
    RecyclerView.Adapter<SearchAdapter.ListViewHolder>() {
    inner class ListViewHolder(private val binding: ListRowSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(gameList: GameList) {
            with(binding) {
                val rating = "${String.format("%.1f", gameList.rating)} ★"

                Glide.with(root.context).load(gameList.image).apply(
                    RequestOptions().error(R.drawable.ic_image)
                ).into(ivSearchGameImageRow)
                tvSearchGameNameRow.text = gameList.name
                tvSearchGameRatingRow.text = rating
            }
        }
    }

    private var binding: ListRowSearchBinding? = null
    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        binding = ListRowSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding as ListRowSearchBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        with(holder) {
            bind(gameLists[position])
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(gameLists[position])
            }
        }
    }

    override fun getItemCount(): Int = gameLists.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setSearchResult(gameLists: ArrayList<GameList>) {
        with(this.gameLists) {
            clear()
            addAll(gameLists)
        }
    }

    fun destroy() {
        binding = null
    }

    interface OnItemClickCallback {
        fun onItemClicked(gameList: GameList)
    }
}