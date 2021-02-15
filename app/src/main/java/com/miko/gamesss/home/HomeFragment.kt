package com.miko.gamesss.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.miko.core.data.Resource
import com.miko.core.domain.model.GameList
import com.miko.core.ui.HomeAdapter
import com.miko.gamesss.databinding.FragmentHomeBinding
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private var adapter: HomeAdapter? = null
    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HomeAdapter()

        binding?.run {
            with(rvHome) {
                adapter = adapter
                setHasFixedSize(true)
            }
        }

        homeViewModel.getGameList().observe(viewLifecycleOwner, { result ->
            if (result != null) {
                when (result) {
                    is Resource.Loading -> {
                        showLoadingScreen(true)
                    }

                    is Resource.Error -> {
                        showLoadingScreen(false)
                        binding?.run {
                            with(errorHome) {
                                root.visibility = View.VISIBLE
                                tvError.text = result.message
                            }
                        }
                    }

                    is Resource.Success -> {
                        showLoadingScreen(false)
                        updateRecyclerList(result.data as List<GameList>)
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        binding?.rvHome?.adapter = null
        binding = null
        adapter?.destroy()

        super.onDestroyView()
    }

    private fun showLoadingScreen(visible: Boolean) {
        binding?.run {
            loadingHome.visibility = if (visible) View.VISIBLE else View.GONE
        }
    }

    private fun updateRecyclerList(gameLists: List<GameList>) {
        adapter?.run {
            setHomeList(ArrayList(gameLists))
            setOnItemClickCallback(object : HomeAdapter.OnItemClickCallback {
                override fun onItemClicked(gameList: GameList) {
                    val toDetailActivity =
                        HomeFragmentDirections.actionHomeFragmentToDetailActivity(gameList.id)

                    view?.findNavController()?.navigate(toDetailActivity)
                }

            })
        }

        binding?.run {
            rvHome.adapter = adapter
        }
    }

}