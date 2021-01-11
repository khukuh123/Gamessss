package com.miko.gamesss.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.miko.gamesss.R
import com.miko.gamesss.databinding.SearchListRowBinding
import com.miko.gamesss.model.GameList

class SearchAdapter(private val gameLists: ArrayList<GameList>) :
    RecyclerView.Adapter<SearchAdapter.ListViewHolder>() {
    inner class ListViewHolder(private val binding: SearchListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(gameList: GameList, onItemClickCallback: OnItemClickCallback) {
            with(binding){
                Glide.with(root.context).load(gameList.image).apply(
                    RequestOptions().error(R.drawable.ic_image)
                ).into(ivSearchGameImageRow)
                ivSearchGameImageRow.setOnClickListener {
                    onItemClickCallback.onItemClicked(gameList)
                }
                tvSearchGameNameRow.text = gameList.name
                val rating = "${String.format("%.1f", gameList.rating)}★"
                tvSearchGameRatingRow.text = rating
            }
        }
    }

    private var binding: SearchListRowBinding? = null
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setGameLists(newData: ArrayList<GameList>) {
        gameLists.clear()
        gameLists.addAll(newData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        binding = SearchListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding as SearchListRowBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(gameLists[position], onItemClickCallback)
    }

    override fun getItemCount(): Int = gameLists.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun destroy() {
        binding = null
    }

    interface OnItemClickCallback{
        fun onItemClicked(gameList: GameList)
    }
}