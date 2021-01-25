package com.miko.gamesss.ui.favorite

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
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
import com.miko.gamesss.model.GameList
import com.miko.gamesss.utils.EspressoIdlingResource
import com.miko.gamesss.viewmodel.ViewModelFactory
import org.junit.After
import org.junit.Before
import org.junit.Test

class FavoriteFragmentTest {

    private lateinit var gameLists: List<GameList>

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.espressoTestIdlingResource)
        val activity = ActivityScenario.launch(MainActivity::class.java)
        activity.onActivity {
            val factory = ViewModelFactory.getInstance(it)
            val favoriteViewModel = ViewModelProvider(it, factory)[FavoriteViewModel::class.java]
            favoriteViewModel.getFavoriteGames().observe(it, { data ->
                gameLists = data
            })
        }
    }

    @After
    fun tearUp() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.espressoTestIdlingResource)
    }

    @Test
    fun clickViewHolderTest() {
        onView(withId(R.id.favoriteFragment)).perform(click())
        onView(withId(R.id.homeFragment)).perform(click())
        onView(withId(R.id.rvHome)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.fabFavorite)).perform(click())
        Espresso.pressBack()
        onView(withId(R.id.favoriteFragment)).perform(click())
        onView(withId(R.id.rvFavorite)).check(matches(isDisplayed()))
        onView(withId(R.id.rvFavorite)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.fabFavorite)).perform(click())
    }

    @Test
    fun favoriteRecyclerViewTestAndFavoriteDeleteTest() {
        onView(withId(R.id.favoriteFragment)).perform(click())
        onView(withId(R.id.homeFragment)).perform(click())
        onView(withId(R.id.rvHome)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.fabFavorite)).perform(click())
        Espresso.pressBack()
        onView(withId(R.id.favoriteFragment)).perform(click())
        onView(withId(R.id.rvFavorite)).check(matches(isDisplayed()))
        onView(withId(R.id.rvFavorite)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                scrollTo()
            )
        )
        onView(withId(R.id.btnDelete)).perform(click())
        onView(withId(R.id.rvFavorite)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                clickViewInViewHolderWithId(R.id.ivClearButton)
            )
        )
        onView(withId(R.id.btnDelete)).perform(click())
    }
}