package com.miko.gamesss

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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
import com.miko.gamesss.ui.favorite.FavoriteFragment
import com.miko.gamesss.ui.search.SearchFragment

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private lateinit var navigationController: NavController
    private lateinit var navigationHostFragment: NavHostFragment
    private var searchViewItem: MenuItem? = null
    private var pressedTime: Long = -1
    private var mMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.run {
            val navigationView = navigationView
            this@MainActivity.navigationHostFragment =
                supportFragmentManager.findFragmentById(R.id.navigationHostFragment) as NavHostFragment
            navigationController = this@MainActivity.navigationHostFragment.navController
            val appBarConfiguration = AppBarConfiguration.Builder(
                R.id.homeFragment,
                R.id.favoriteFragment
            ).build()

            setupActionBarWithNavController(navigationController, appBarConfiguration)
            navigationView.setupWithNavController(navigationController)
        }
    }

    override fun onBackPressed() {
        if (navigationHostFragment.childFragmentManager.backStackEntryCount == 0) {
            if ((pressedTime + 2000 > System.currentTimeMillis())) {
                super.onBackPressed()
            } else {
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
            }
            pressedTime = System.currentTimeMillis()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_action_bar_menu, menu)

        mMenu = menu

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchViewItem = menu?.findItem(R.id.btnSearch)
        navigationController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id != R.id.searchFragment) {
                if (searchViewItem?.isActionViewExpanded as Boolean) {
                    searchViewItem?.collapseActionView()
                }
            }
            if (destination.id == R.id.favoriteFragment) {
                menu?.findItem(R.id.btnDelete)?.isVisible = true
            } else if (destination.id != R.id.favoriteFragment) {
                menu?.findItem(R.id.btnDelete)?.isVisible = false
            }
        }
        val searchView = searchViewItem?.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE

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
                    searchView.clearFocus()
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
            R.id.btnDelete -> {
                val buttonDelete = mMenu?.findItem(R.id.btnDelete)
                if (buttonDelete != null) {
                    val favoriteFragment =
                        navigationHostFragment.childFragmentManager.primaryNavigationFragment as FavoriteFragment
                    favoriteFragment.updateList()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}