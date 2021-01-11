package com.miko.gamesss

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.miko.gamesss.databinding.ActivityMainBinding
import com.miko.gamesss.ui.search.SearchFragment

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private lateinit var navigationController: NavController
    private var searchViewItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.run {
            val navigationView = navigationView
            val navigationHostFragment =
                supportFragmentManager.findFragmentById(R.id.navigationHostFragment) as NavHostFragment
            navigationController = navigationHostFragment.navController
            val appBarConfiguration = AppBarConfiguration.Builder(
                R.id.homeFragment,
                R.id.favoriteFragment
            ).build()

            setupActionBarWithNavController(navigationController, appBarConfiguration)
            navigationView.setupWithNavController(navigationController)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchViewItem = menu?.findItem(R.id.btnSearch)
        navigationController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id != R.id.searchFragment) {
                if (searchViewItem?.isActionViewExpanded as Boolean) {
                    searchViewItem?.collapseActionView()
                }
            }
        }
        val searchView = searchViewItem?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "ex. Ronaldinho Soccer"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty()) {
                    val queryBundle = bundleOf(SearchFragment.QUERY to query)
                    navigationController.navigate(
                        R.id.searchFragment,
                        queryBundle,
                        NavOptions.Builder().setLaunchSingleTop(true).build()
                    )
                    hideKeyBoard()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false

        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun hideKeyBoard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val rootView = binding?.root
        if (rootView != null) {
            imm.hideSoftInputFromWindow(rootView.windowToken, 0)
            rootView.clearFocus()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                navigationController.popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}