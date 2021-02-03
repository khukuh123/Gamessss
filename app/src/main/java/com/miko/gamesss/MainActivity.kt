package com.miko.gamesss

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.dynamicfeatures.fragment.DynamicNavHostFragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.miko.gamesss.databinding.ActivityMainBinding
import com.miko.gamesss.search.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var navigationController: NavController
    private lateinit var navigationHostFragment: NavHostFragment
    private var binding: ActivityMainBinding? = null
    private var searchViewItem: MenuItem? = null
    private var pressedTime: Long = -1
    private var mMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.run {
            this@MainActivity.navigationHostFragment =
                supportFragmentManager.findFragmentById(R.id.navigationHostFragment) as DynamicNavHostFragment
            navigationController = this@MainActivity.navigationHostFragment.navController
            val appBarConfiguration = AppBarConfiguration.Builder(
                R.id.homeFragment,
                R.id.favoriteFragment,
                R.id.searchFragment
            ).build()

            setupActionBarWithNavController(navigationController, appBarConfiguration)

            fabToFavorite.setOnClickListener {
                installModule()
            }
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
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        menuInflater.inflate(R.menu.main_action_bar_menu, menu)
        mMenu = menu

        searchViewItem = menu?.findItem(R.id.btnSearch)
        navigationController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id != R.id.searchFragment) {
                if (searchViewItem?.isActionViewExpanded as Boolean) {
                    searchViewItem?.collapseActionView()
                }
            }
            binding?.run {
                fabToFavorite.visibility =
                    if (destination.id == R.id.favoriteFragment) View.GONE else View.VISIBLE
            }
        }

        val searchView = searchViewItem?.actionView as SearchView

        with(searchView) {
            maxWidth = Int.MAX_VALUE
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            queryHint = "ex. Ronaldinho Soccer"
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        }

        return super.onCreateOptionsMenu(menu)
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

    private fun hideKeyBoard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val rootView = binding?.root?.rootView

        if (rootView != null) {
            imm.hideSoftInputFromWindow(rootView.windowToken, 0)
            rootView.clearFocus()
        }
    }

    private fun moveToFavoriteFragment() {
        navigationController.navigate(R.id.favoriteFragment)
    }

    private fun installModule() {
        val splitInstallManager = SplitInstallManagerFactory.create(this)
        val favoriteModule = "favorite"
        if (splitInstallManager.installedModules.contains(favoriteModule)) {
            Toast.makeText(this, "Opening Favorite Module...", Toast.LENGTH_SHORT).show()
            moveToFavoriteFragment()
        } else {
            val request = SplitInstallRequest.newBuilder().addModule(favoriteModule).build()

            splitInstallManager.startInstall(request).addOnCompleteListener {
                Toast.makeText(this, "Success installing module", Toast.LENGTH_SHORT).show()
                moveToFavoriteFragment()
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Failed installing module\n${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}