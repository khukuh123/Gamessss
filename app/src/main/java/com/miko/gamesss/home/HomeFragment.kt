package com.miko.gamesss.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    override fun onResume() {
        super.onResume()
        homeViewModel.setGameList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvHome?.setHasFixedSize(true)
        homeViewModel.setGameList()
        homeViewModel.getGameList().observe(viewLifecycleOwner, { result ->
            if (result != null) {
                when (result) {
                    is Resource.Loading -> {
                        showLoadingScreen(true)
                    }

                    is Resource.Error -> {
                        showLoadingScreen(false)
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
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
        super.onDestroyView()
        binding = null
        adapter?.destroy()
    }

    private fun showLoadingScreen(visible: Boolean) {
        binding?.run {
            loadingHome.root.visibility = if (visible) View.VISIBLE else View.GONE
        }
    }

    private fun updateRecyclerList(gameLists: List<GameList>) {
        adapter = HomeAdapter(ArrayList(gameLists))
        adapter?.setOnItemClickCallback(object : HomeAdapter.OnItemClickCallback {
            override fun onItemClicked(gameList: GameList) {
                val toDetailActivity =
                    HomeFragmentDirections.actionHomeFragmentToDetailActivity(gameList.id)

                view?.findNavController()?.navigate(toDetailActivity)
            }

        })

        binding?.run {
            rvHome.adapter = adapter
        }
    }

}