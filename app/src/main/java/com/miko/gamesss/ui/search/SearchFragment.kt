package com.miko.gamesss.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.miko.gamesss.databinding.FragmentSearchBinding
import com.miko.gamesss.core.domain.model.GameList
import com.miko.gamesss.core.ui.SearchAdapter
import com.miko.gamesss.core.ui.ViewModelFactory
import com.miko.gamesss.core.vo.Status

class SearchFragment : Fragment() {

    companion object {
        const val QUERY = "query"
    }

    private var binding: FragmentSearchBinding? = null
    private var adapter: SearchAdapter? = null
    private lateinit var searchViewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.run {
            rvSearch.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvSearch.setHasFixedSize(true)
        }

        adapter = SearchAdapter(arrayListOf())

        val factory = ViewModelFactory.getInstance(requireContext())
        searchViewModel = ViewModelProvider(this, factory)[SearchViewModel::class.java]

        val query = arguments?.getString(QUERY)
        if (query != null) {
            binding?.run {
                searchViewModel.setSearchQuery(query)
                searchViewModel.getSearchResult().observe(viewLifecycleOwner, { result ->
                    if (result != null) {
                        when (result.status) {
                            Status.SUCCESS -> {
                                val data = result.data as List<GameList>
                                updateRecyclerList(ArrayList(data))
                                showLoadingScreen(false)
                            }
                            Status.LOADING -> {
                                showLoadingScreen(true)
                            }
                            Status.ERROR -> {
                                showLoadingScreen(false)
                                val data = result.data as List<GameList>
                                updateRecyclerList(ArrayList(data))
                                Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                })
            }
        }
    }

    private fun showLoadingScreen(visible: Boolean) {
        binding?.run {
            loadingSearch.root.visibility = if (visible) View.VISIBLE else View.GONE
        }
    }

    private fun updateRecyclerList(gameLists: ArrayList<GameList>) {
        adapter?.run {
            setGameLists(gameLists)
            setOnItemClickCallback(object : SearchAdapter.OnItemClickCallback {
                override fun onItemClicked(gameList: GameList) {
                    val toDetailActivity =
                        SearchFragmentDirections.actionSearchFragmentToDetailActivity(gameList.id)
                    view?.findNavController()?.navigate(toDetailActivity)
                }
            })
        }
        binding?.run {
            rvSearch.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        adapter?.destroy()
    }

}