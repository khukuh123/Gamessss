package com.miko.gamesss.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.miko.core.domain.model.GameList
import com.miko.core.ui.FavoriteAdapter
import com.miko.gamesss.databinding.FragmentFavoriteBinding
import org.koin.android.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    private var binding: FragmentFavoriteBinding? = null
    private var adapter: FavoriteAdapter? = null
    private val favoriteViewModel: FavoriteViewModel by viewModel()
    private var clearButtonState = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater)
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.getFavoriteGames().observe(viewLifecycleOwner, { data ->
            updateRecyclerList(ArrayList(data))
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FavoriteAdapter(arrayListOf())

        binding?.run {
            rvFavorite.setHasFixedSize(true)
            val mLayoutManager = LinearLayoutManager(requireContext())
            rvFavorite.layoutManager = mLayoutManager
            favoriteViewModel.getFavoriteGames().observe(viewLifecycleOwner, { data ->
                updateRecyclerList(ArrayList(data))
            })
        }
    }

    fun updateList() {
        adapter?.clearButtonState = !clearButtonState
        favoriteViewModel.getFavoriteGames().observe(viewLifecycleOwner, { data ->
            updateRecyclerList(ArrayList(data))
        })
        clearButtonState = !clearButtonState
    }

    private fun updateRecyclerList(gameLists: ArrayList<GameList>) {
        adapter?.setGameLists(gameLists)
        adapter?.setOnClickCallback(object : FavoriteAdapter.OnClickCallback {
            override fun onItemClicked(gameList: GameList) {
                val toDetailActivity =
                    FavoriteFragmentDirections.actionFavoriteFragmentToDetailActivity(gameList.id)
                view?.findNavController()?.navigate(toDetailActivity)
            }

            override fun onClearButtonClicked(gameList: GameList) {
                favoriteViewModel.deleteFavoriteGame(gameList.id)
                favoriteViewModel.getFavoriteGames().observe(viewLifecycleOwner, { data ->
                    updateRecyclerList(ArrayList(data))
                })
            }
        })
        binding?.rvFavorite?.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        adapter?.destroy()
    }
}