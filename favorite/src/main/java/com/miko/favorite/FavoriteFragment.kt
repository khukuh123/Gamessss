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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater)
        loadKoinModules(favoriteModule)
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
            val mLayoutManager = LinearLayoutManager(requireContext())

            with(rvFavorite) {
                setHasFixedSize(true)
                layoutManager = mLayoutManager
            }
            favoriteViewModel.getFavoriteGames().observe(viewLifecycleOwner, { data ->
                updateRecyclerList(ArrayList(data))
            })
            btnDelete.setOnClickListener {
                updateList()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        adapter?.destroy()
    }

    private fun updateList() {
        adapter?.clearButtonState = !clearButtonState
        favoriteViewModel.getFavoriteGames().observe(viewLifecycleOwner, { data ->
            updateRecyclerList(ArrayList(data))
        })

        clearButtonState = !clearButtonState
    }

    private fun updateRecyclerList(gameLists: ArrayList<GameList>) {
        adapter?.run {
            setGameLists(gameLists)
            setOnClickCallback(object : FavoriteAdapter.OnClickCallback {
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
        }

        binding?.rvFavorite?.adapter = adapter
    }
}