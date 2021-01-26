package com.miko.gamesss.ui.home

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
import com.miko.gamesss.EspressoCustomMethod.clickViewInViewHolderWithId
import com.miko.gamesss.MainActivity
import com.miko.gamesss.R
import com.miko.gamesss.core.domain.model.GameList
import com.miko.gamesss.core.utils.EspressoIdlingResource
import com.miko.gamesss.core.ui.ViewModelFactory
import com.miko.gamesss.core.vo.Status
import org.junit.After
import org.junit.Before
import org.junit.Test

class HomeFragmentTest {

    private lateinit var gameLists: List<GameList>

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.espressoTestIdlingResource)
        val activity = ActivityScenario.launch(MainActivity::class.java)
        activity.onActivity {
            val factory = ViewModelFactory.getInstance(it)
            val homeViewModel = ViewModelProvider(it, factory)[HomeViewModel::class.java]
            homeViewModel.setGameList()
            homeViewModel.getGameList().observe(it, { result ->
                if (result != null) {
                    when (result.status) {
                        Status.SUCCESS -> {
                            gameLists = result.data as List<GameList>
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
    fun homeRecyclerViewTest() {
        onView(withId(R.id.homeFragment)).perform(click())
        onView(withId(R.id.rvHome)).check(matches(isDisplayed()))
        onView(withId(R.id.rvHome)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                gameLists.size - 1,
                scrollTo()
            )
        )
    }

    @Test
    fun clickViewHolderTest() {
        onView(withId(R.id.homeFragment)).perform(click())
        onView(withId(R.id.rvHome)).check(matches(isDisplayed()))
        onView(withId(R.id.rvHome)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                clickViewInViewHolderWithId(R.id.ivGameImageRow)
            )
        )
    }
}