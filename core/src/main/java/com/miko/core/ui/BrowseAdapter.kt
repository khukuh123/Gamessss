package com.miko.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.miko.core.R
import com.miko.core.databinding.ListRowBrowseBinding
import com.miko.core.domain.model.GameList

class BrowseAdapter(private val gameLists: ArrayList<GameList>) :
    RecyclerView.Adapter<BrowseAdapter.ListViewHolder>() {
    inner class ListViewHolder(private val binding: ListRowBrowseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(gameList: GameList) {
            with(binding) {
                val rating = "${String.format("%.1f", gameList.rating)} ★"

                Glide.with(binding.root.context).load(gameList.image)
                    .apply(RequestOptions().error(R.drawable.ic_image)).into(ivBrowseGameImageRow)
                tvBrowseGameNameRow.text = gameList.name
                tvBrowseGameRatingRow.text = rating
                tvBrowseMetaCritic.text = gameList.metaCritic.toString()
            }
        }
    }

    private var binding: ListRowBrowseBinding? = null
    private lateinit var onItemClickCallBack: OnItemClickCallBack

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        binding = ListRowBrowseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding as ListRowBrowseBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        with(holder) {
            bind(gameLists[position])
            itemView.setOnClickListener {
                onItemClickCallBack.onItemClicked(gameLists[position])
            }
        }
    }

    override fun getItemCount(): Int = gameLists.size

    fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    fun setGameLists(newData: ArrayList<GameList>) {
        gameLists.clear()
        gameLists.addAll(newData)
    }

    fun destroy() {
        binding = null
    }

    interface OnItemClickCallBack {
        fun onItemClicked(gameList: GameList)
    }
}