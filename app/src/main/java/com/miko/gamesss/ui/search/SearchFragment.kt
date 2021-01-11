package com.miko.gamesss.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.miko.gamesss.databinding.FragmentSearchBinding
import com.miko.gamesss.model.GameList
import com.miko.gamesss.viewmodel.ViewModelFactory
import com.miko.gamesss.vo.Status

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
                            }
                            Status.LOADING -> {

                            }
                            Status.ERROR -> {

                            }
                        }
                    }
                })
            }
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