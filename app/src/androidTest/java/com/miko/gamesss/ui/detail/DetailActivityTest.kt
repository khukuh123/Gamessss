package com.miko.gamesss.ui.detail

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.miko.gamesss.EspressoCustomMethod.typeSearchViewText
import com.miko.gamesss.EspressoCustomMethod.withChildName
import com.miko.gamesss.EspressoCustomMethod.withCtlTitle
import com.miko.gamesss.EspressoCustomMethod.withIndex
import com.miko.gamesss.MainActivity
import com.miko.gamesss.R
import com.miko.gamesss.model.GameDetail
import com.miko.gamesss.model.Section
import com.miko.gamesss.utils.EspressoIdlingResource
import com.miko.gamesss.viewmodel.ViewModelFactory
import com.miko.gamesss.vo.Status
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Before
import org.junit.Test

class DetailActivityTest {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var gameDetail: GameDetail

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.espressoTestIdlingResource)
        val activity = ActivityScenario.launch(MainActivity::class.java)
        activity.onActivity {
            val factory = ViewModelFactory.getInstance(it)
            detailViewModel = ViewModelProvider(it, factory)[DetailViewModel::class.java]
            detailViewModel.getGameDetail(51610.toString()).observe(it, { result ->
                if (result != null) {
                    when (result.status) {
                        Status.SUCCESS -> {
                            gameDetail = result.data as GameDetail
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
    fun detailActivityTest() {
        onView(withIndex(withId(R.id.btnSearch), 0)).perform(click())
        onView(withIndex(withId(R.id.btnSearch), 0)).perform(typeSearchViewText(gameDetail.name))
        onView(withId(R.id.rvSearch)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(
            allOf(
                instanceOf(CollapsingToolbarLayout::class.java),
                withChild(withId(R.id.myToolbar))
            )
        ).check(
            matches(withCtlTitle(gameDetail.name))
        )
        val rating = "${String.format("%.1f", gameDetail.rating)} ★"
        onView(withId(R.id.tvRatingDetail)).check(matches(withText(rating)))
        onView(withId(R.id.tvGenresDetail)).check(matches(withText(gameDetail.genres)))
        onView(withId(R.id.expandable_text)).check(matches(withText(gameDetail.description)))
        onView(isRoot()).perform(swipeUp())
        for (i in gameDetail.listSection.indices) {
            for (j in gameDetail.listSection[i].listItem.indices) {
                onData(
                    allOf(
                        `is`(instanceOf(Section::class.java)),
                        withChildName(gameDetail.listSection[i].listItem[j].name)
                    )
                ).inAdapterView(withId(R.id.elvDetail)).check(matches(isDisplayed()))
            }
        }
    }
}