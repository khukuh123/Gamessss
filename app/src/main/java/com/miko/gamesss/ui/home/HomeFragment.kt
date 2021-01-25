package com.miko.gamesss.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.miko.gamesss.databinding.FragmentHomeBinding
import com.miko.gamesss.model.GameList
import com.miko.gamesss.viewmodel.ViewModelFactory
import com.miko.gamesss.vo.Status

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private var adapter: HomeAdapter? = null
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = ViewModelFactory.getInstance(requireContext())
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        homeViewModel.setGameList()
        homeViewModel.getGameList().observe(viewLifecycleOwner, { result ->
            if (result != null) {
                when (result.status) {
                    Status.LOADING -> {
                        showLoadingScreen(true)
                    }
                    Status.ERROR -> {
                        showLoadingScreen(false)
                        updateRecyclerList(result.data as List<GameList>)
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        showLoadingScreen(false)
                        updateRecyclerList(result.data as List<GameList>)
                    }
                }
            }
        })
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
            rvHome.setHasFixedSize(true)
            rvHome.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (binding != null) {
            binding = null
        }
        adapter?.destroy()
    }

}