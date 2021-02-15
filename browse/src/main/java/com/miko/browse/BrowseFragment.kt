package com.miko.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.miko.browse.databinding.FragmentBrowseBinding
import com.miko.browse.di.browseModule
import com.miko.core.data.Resource
import com.miko.core.domain.model.GameList
import com.miko.core.ui.BrowseAdapter
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class BrowseFragment : Fragment() {

    private var binding: FragmentBrowseBinding? = null
    private var lastIndex = -1
    private val browseViewModel: BrowseViewModel by viewModel()
    private var adapter: BrowseAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        loadKoinModules(browseModule)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBrowseBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BrowseAdapter()

        binding?.run {
            val items = resources.getStringArray(R.array.sort_list_item)
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.list_item, items)

            with(rvBrowse) {
                adapter = adapter
                setHasFixedSize(true)
            }

            (tilSortBy.editText as? AutoCompleteTextView)?.run {
                setAdapter(arrayAdapter)
                setOnItemClickListener { _, _, _, l ->
                    if (l.toInt() != lastIndex) {
                        rvBrowse.adapter = null
                        browseViewModel.getGameListSorted(l.toInt())
                            .observe(viewLifecycleOwner, { result ->
                                when (result) {
                                    is Resource.Success -> {
                                        showLoadingScreen(false)
                                        updateRecyclerList(ArrayList(result.data as List<GameList>))
                                    }

                                    is Resource.Loading -> showLoadingScreen(true)

                                    is Resource.Error -> {
                                        showLoadingScreen(false)
                                        binding?.run {
                                            with(errorBrowse) {
                                                root.visibility = View.VISIBLE
                                                tvError.text = result.message
                                            }
                                        }
                                    }
                                }
                                browseCompass.root.visibility = View.GONE

                                lastIndex = l.toInt()
                            })
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        binding?.run {
            rvBrowse.adapter = null
            (tilSortBy.editText as? AutoCompleteTextView)?.setAdapter(null)
        }
        binding = null
        adapter?.destroy()

        super.onDestroyView()
    }

    private fun showLoadingScreen(visible: Boolean) {
        binding?.run {
            loadingBrowse.visibility = if (visible) View.VISIBLE else View.GONE
        }
    }

    private fun updateRecyclerList(gameLists: ArrayList<GameList>) {
        adapter?.run {
            setGameLists(gameLists)
            setOnItemClickCallback(object : BrowseAdapter.OnItemClickCallBack {
                override fun onItemClicked(gameList: GameList) {
                    val toDetailActivity =
                        BrowseFragmentDirections.actionBrowseFragment2ToDetailActivity(gameList.id)
                    view?.findNavController()?.navigate(toDetailActivity)
                }
            })
        }

        binding?.rvBrowse?.adapter = adapter
    }

}