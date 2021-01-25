package com.miko.gamesss.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.miko.gamesss.R
import com.miko.gamesss.databinding.ListRowHomeBinding
import com.miko.gamesss.model.GameList

class HomeAdapter(private val gameLists: ArrayList<GameList>) :
    RecyclerView.Adapter<HomeAdapter.ListViewHolder>() {
    inner class ListViewHolder(private val binding: ListRowHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(gameList: GameList, onItemClickCallback: OnItemClickCallback) {
            with(binding) {
                Glide.with(root).load(gameList.image).apply(
                    RequestOptions().error(R.drawable.ic_image)
                ).into(ivGameImageRow)
                ivGameImageRow.setOnClickListener {
                    onItemClickCallback.onItemClicked(gameList)
                }
                tvGameNameRow.text = gameList.name
                val rating = "${String.format("%.1f", gameList.rating)} ★"
                tvRatingRow.text = rating
            }
        }
    }

    private var binding: ListRowHomeBinding? = null
    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        binding = ListRowHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding as ListRowHomeBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(gameLists[position], onItemClickCallback)
    }

    override fun getItemCount(): Int = gameLists.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun destroy() {
        binding = null
    }

    interface OnItemClickCallback {
        fun onItemClicked(gameList: GameList)
    }
}