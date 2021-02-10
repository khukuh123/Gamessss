package com.miko.gamesss.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.miko.core.data.Resource
import com.miko.core.domain.model.GameDetail
import com.miko.core.ui.DetailSectionAdapter
import com.miko.gamesss.R
import com.miko.gamesss.databinding.ActivityDetailBinding
import org.koin.android.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {
    private var binding: ActivityDetailBinding? = null
    private var gameId = -1
    private var adapter: DetailSectionAdapter? = null
    private var mMenu: Menu? = null
    private var btnFavoriteState = false
    private val detailViewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        gameId = DetailActivityArgs.fromBundle(intent.extras as Bundle).gameId

        detailViewModel.getGameDetail("$gameId").observe(this, { result ->
            val gameDetail: GameDetail?

            if (result != null) {
                when (result) {
                    is Resource.Success -> {
                        gameDetail = result.data

                        if (gameDetail != null) {
                            setGameDetail(gameDetail)
                        }

                        showLoadingScreen(false)
                    }

                    is Resource.Error -> {
                        Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                        showLoadingScreen(false)
                    }

                    is Resource.Loading -> {
                        showLoadingScreen(true)
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_action_bar_menu, menu)
        mMenu = menu as Menu

        binding?.run {
            appBarDetail.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
                if (mMenu != null) {
                    val btnFavorite = mMenu?.findItem(R.id.btnFavorite)

                    btnFavorite?.isVisible = verticalOffset != 0
                }
            })
            fabFavorite.setOnClickListener {
                if (!checkFavoriteModule()) {
                    setFavoriteState(!btnFavoriteState)
                    detailViewModel.setFavoriteState(gameId, btnFavoriteState)
                }
            }
        }

        detailViewModel.checkGameFavoriteStatus(gameId).observe(this@DetailActivity, { data ->
            if (data.isNotEmpty()) {
                setFavoriteState(true)
            } else {
                setFavoriteState(false)
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        binding = null
        adapter?.destroy()

        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }

            R.id.btnFavorite -> {
                if (!checkFavoriteModule()) {
                    setFavoriteState(!btnFavoriteState)
                    detailViewModel.setFavoriteState(gameId, btnFavoriteState)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun checkFavoriteModule(): Boolean {
        val isNotInstalled =
            !SplitInstallManagerFactory.create(this).installedModules.contains("favorite")
        if (isNotInstalled) {
            Toast.makeText(
                this@DetailActivity,
                "Favorite Module not found",
                Toast.LENGTH_SHORT
            ).show()
        }
        return isNotInstalled
    }

    private fun setGameDetail(gameDetail: GameDetail) {
        binding?.run {
            setSupportActionBar(myToolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            ctlDetail.title = gameDetail.name
            val rating = "${String.format("%.1f", gameDetail.rating)} ★"
            tvRatingDetail.text = rating
            tvMetaCriticDetail.text = gameDetail.metaCritic.toString()
            tvGenresDetail.text = gameDetail.genres
            Glide.with(this@DetailActivity).load(gameDetail.backgroundImage).apply(
                RequestOptions().error(
                    R.drawable.ic_image
                )
            ).into(ivDetailGame)
            etvAboutDetail.text = gameDetail.description

            adapter = DetailSectionAdapter(gameDetail.listSection)
            elvDetail.setAdapter(adapter)

            setListViewHeightBasedOnChildren(elvDetail, true)

            elvDetail.setOnGroupExpandListener(object : ExpandableListView.OnGroupExpandListener {
                var previousGroup = -1

                override fun onGroupExpand(groupPosition: Int) {
                    if ((previousGroup != -1) && (groupPosition != previousGroup)) {
                        elvDetail.collapseGroup(previousGroup)
                    }

                    previousGroup = groupPosition
                    setListViewHeightBasedOnChildren(elvDetail, false)
                }
            })
            elvDetail.setOnGroupCollapseListener {
                setListViewHeightBasedOnChildren(elvDetail, true)
            }
        }

    }

    private fun setFavoriteState(isFavorite: Boolean) {
        if (mMenu != null) {
            val btnFavorite = mMenu?.findItem(R.id.btnFavorite)

            if (isFavorite) {
                btnFavorite?.setIcon(R.drawable.ic_favorite)
                binding?.fabFavorite?.setImageResource(R.drawable.ic_favorite)
            } else {
                btnFavorite?.setIcon(R.drawable.ic_favorite_border)
                binding?.fabFavorite?.setImageResource(R.drawable.ic_favorite_border)
            }
        }

        btnFavoriteState = isFavorite
    }

    private fun showLoadingScreen(visible: Boolean) {
        binding?.run {

            if (visible) {
                loadingDetail.root.visibility = View.VISIBLE
                fabFavorite.visibility = View.GONE
            } else {
                loadingDetail.root.visibility = View.GONE
                fabFavorite.visibility = View.VISIBLE
            }

        }
    }

    private fun setListViewHeightBasedOnChildren(elv: ExpandableListView, isExpanded: Boolean) {
        var totalHeight = 0
        val params = elv.layoutParams

        if (isExpanded) {
            val groupCount = adapter?.groupCount

            if (groupCount != null) {

                for (i in 0 until groupCount) {
                    val listItem = adapter?.getGroupView(i, false, null, elv)

                    listItem?.measure(0, 0)
                    totalHeight += (listItem?.measuredHeight as Int)
                }

                params.height = totalHeight + (elv.dividerHeight * (groupCount - 1))
            }
        } else {
            val item = adapter?.getChildView(0, 0, true, null, elv)

            item?.measure(0, 0)
            totalHeight += item?.measuredHeight as Int
            params.height += totalHeight
        }

        elv.layoutParams = params
    }
}