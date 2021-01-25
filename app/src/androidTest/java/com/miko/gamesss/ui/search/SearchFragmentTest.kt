package com.miko.gamesss.ui.search

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.miko.gamesss.EspressoCustomMethod.typeSearchViewText
import com.miko.gamesss.EspressoCustomMethod.withIndex
import com.miko.gamesss.MainActivity
import com.miko.gamesss.R
import com.miko.gamesss.model.GameList
import com.miko.gamesss.utils.EspressoIdlingResource
import com.miko.gamesss.viewmodel.ViewModelFactory
import com.miko.gamesss.vo.Status
import org.junit.After
import org.junit.Before
import org.junit.Test

class SearchFragmentTest {

    private lateinit var gameList: List<GameList>

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.espressoTestIdlingResource)
        val activity = ActivityScenario.launch(MainActivity::class.java)
        activity.onActivity {
            val factory = ViewModelFactory.getInstance(it)
            val searchViewModel = ViewModelProvider(it, factory)[SearchViewModel::class.java]
            searchViewModel.setSearchQuery("Dark Souls")
            searchViewModel.getSearchResult().observe(it, { result ->
                if (result != null) {
                    when (result.status) {
                        Status.SUCCESS -> {
                            gameList = result.data as List<GameList>
                        }
                        else -> {
                        }
                    }
                }
            })
        }
    }

    @After
    fun tearUp() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.espressoTestIdlingResource)
    }

    @Test
    fun searchRecyclerViewTest() {
        onView(withIndex(withId(R.id.btnSearch), 0)).perform(click())
        onView(withIndex(withId(R.id.btnSearch), 0)).perform(typeSearchViewText("Dark Souls"))
        onView(withId(R.id.rvSearch)).check(matches(isDisplayed()))
        onView(withId(R.id.rvSearch)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                gameList.size - 1,
                scrollTo()
            )
        )
    }

    @Test
    fun clickViewHolderTest() {
        onView(withIndex(withId(R.id.btnSearch), 0)).perform(click())
        onView(withIndex(withId(R.id.btnSearch), 0)).perform(typeSearchViewText("Dark Souls"))
        onView(withId(R.id.rvSearch)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
    }
}