package com.miko.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.miko.core.R
import com.miko.core.databinding.ListRowFavoriteBinding
import com.miko.core.domain.model.GameList

class FavoriteAdapter(private val gameLists: ArrayList<GameList>) :
    RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>() {
    inner class ListViewHolder(private val binding: ListRowFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            gameList: GameList,
            onClickCallback: OnClickCallback,
            clearButtonVisible: Boolean
        ) {
            binding.run {
                val rating = "${String.format("%.1f", gameList.rating)} ★"

                tvFavoriteGameRatingRow.text = rating
                tvFavoriteGameNameRow.text = gameList.name
                Glide.with(binding.root.context).load(gameList.image)
                    .apply(RequestOptions().error(R.drawable.ic_image)).into(ivFavoriteGameImageRow)
                with(ivClearButton) {
                    setOnClickListener {
                        onClickCallback.onClearButtonClicked(gameList)
                    }
                    visibility = if (clearButtonVisible) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private lateinit var onClickCallback: OnClickCallback
    private var binding: ListRowFavoriteBinding? = null
    var clearButtonState = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        binding = ListRowFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding as ListRowFavoriteBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        with(holder) {
            bind(gameLists[position], onClickCallback, clearButtonState)
            itemView.setOnClickListener {
                onClickCallback.onItemClicked(gameLists[position])
            }
        }
    }

    override fun getItemCount(): Int = gameLists.size

    fun setGameLists(gameLists: ArrayList<GameList>) {
        this.gameLists.clear()
        this.gameLists.addAll(gameLists)
    }

    fun setOnClickCallback(onClickCallback: OnClickCallback) {
        this.onClickCallback = onClickCallback
    }

    fun destroy() {
        binding = null
    }

    interface OnClickCallback {
        fun onItemClicked(gameList: GameList)

        fun onClearButtonClicked(gameList: GameList)
    }

}