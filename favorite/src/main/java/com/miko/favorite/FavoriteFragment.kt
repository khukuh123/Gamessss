package com.miko.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.miko.core.domain.model.GameList
import com.miko.core.ui.FavoriteAdapter
import com.miko.favorite.databinding.FragmentFavoriteBinding
import com.miko.favorite.di.favoriteModule
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteFragment : Fragment() {

    private var binding: FragmentFavoriteBinding? = null
    private var adapter: FavoriteAdapter? = null
    private val favoriteViewModel: FavoriteViewModel by viewModel()
    private var clearButtonState = false
    private var gameLists: ArrayList<GameList>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        loadKoinModules(favoriteModule)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FavoriteAdapter()

        binding?.run {
            val mLayoutManager = LinearLayoutManager(requireContext())

            with(rvFavorite) {
                layoutManager = mLayoutManager
                adapter = adapter
                setHasFixedSize(true)
            }

            favoriteViewModel.getFavoriteGames().observe(viewLifecycleOwner, { data ->
                gameLists = ArrayList(data)
                if (gameLists?.isEmpty() as Boolean) {
                    binding?.emptyFavorite?.root?.visibility = View.VISIBLE
                }
                updateRecyclerList(gameLists as ArrayList<GameList>)
            })

            btnDelete.setOnClickListener {
                updateList()
            }
        }
    }

    override fun onDestroyView() {
        binding?.rvFavorite?.adapter = null
        binding = null
        adapter?.destroy()

        super.onDestroyView()
    }

    private fun updateList() {
        adapter?.clearButtonState = !clearButtonState

        updateRecyclerList(gameLists as ArrayList<GameList>)

        clearButtonState = !clearButtonState
    }

    private fun updateRecyclerList(gameLists: ArrayList<GameList>) {
        adapter?.run {
            setFavoriteList(gameLists)
            setOnClickCallback(object : FavoriteAdapter.OnClickCallback {
                override fun onItemClicked(gameList: GameList) {
                    val toDetailActivity =
                        FavoriteFragmentDirections.actionFavoriteFragmentToDetailActivity(gameList.id)

                    view?.findNavController()?.navigate(toDetailActivity)
                }

                override fun onClearButtonClicked(gameList: GameList) {
                    favoriteViewModel.deleteFavoriteGame(gameList.id)
                }
            })
        }

        binding?.rvFavorite?.adapter = adapter
    }
}