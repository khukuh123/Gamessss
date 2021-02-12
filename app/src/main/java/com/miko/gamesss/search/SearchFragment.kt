package com.miko.gamesss.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.miko.core.data.Resource
import com.miko.core.domain.model.GameList
import com.miko.core.ui.SearchAdapter
import com.miko.gamesss.databinding.FragmentSearchBinding
import org.koin.android.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    companion object {
        const val QUERY = "query"
    }

    private var binding: FragmentSearchBinding? = null
    private var adapter: SearchAdapter? = null
    private val searchViewModel: SearchViewModel by viewModel()

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

        val query = arguments?.getString(QUERY)
        if (query != null) {
            binding?.run {
                searchViewModel.setSearchQuery(query)
                searchViewModel.getSearchResult().observe(viewLifecycleOwner, { result ->
                    if (result != null) {
                        when (result) {
                            is Resource.Success -> {
                                val data = result.data as List<GameList>
                                updateRecyclerList(ArrayList(data))
                                showLoadingScreen(false)
                            }

                            is Resource.Loading -> {
                                showLoadingScreen(true)
                            }

                            is Resource.Error -> {
                                showLoadingScreen(false)
                                Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    }
                })
            }
        }
    }

    override fun onDestroyView() {
        binding?.rvSearch?.adapter = null
        binding = null
        adapter?.destroy()

        super.onDestroyView()
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

}