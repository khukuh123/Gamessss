package com.miko.gamesss

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import com.miko.gamesss.EspressoCustomMethod.typeSearchViewText
import com.miko.gamesss.EspressoCustomMethod.withIndex
import com.miko.gamesss.utils.EspressoIdlingResource
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test


class MainActivityTest {

    private lateinit var activity: ActivityScenario<MainActivity>
    private lateinit var decorView: View

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.espressoTestIdlingResource)
        activity = ActivityScenario.launch(MainActivity::class.java)
        activity.onActivity {
            decorView = it.window.decorView
        }
    }

    @After
    fun tearUp() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.espressoTestIdlingResource)
    }

    @Test
    fun pressBackAgainToExitTest() {
        onView(withId(R.id.favoriteFragment)).check(matches(isDisplayed())).perform(click())
        onView(withIndex(withId(R.id.btnSearch), 0)).check(matches(isDisplayed())).perform(click())
        onView(withIndex(withId(R.id.btnSearch), 0)).check(matches(isDisplayed())).perform(
            typeSearchViewText("Dark Souls")
        )
        Espresso.pressBack()
        Espresso.pressBack()
        Espresso.pressBack()
        Espresso.pressBack()
        Espresso.pressBack()
        onView(withText("Press back again to exit")).inRoot(withDecorView(not(decorView))).check(
            matches(
                isDisplayed()
            )
        )
    }

}