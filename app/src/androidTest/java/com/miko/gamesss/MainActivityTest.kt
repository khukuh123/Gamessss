package com.miko.gamesss

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.contrib.RecyclerViewActions
import com.miko.gamesss.utils.DataDummy
import org.junit.Before
import org.junit.Test

class MainActivityTest{

    private val dummy = DataDummy.generateGameList()

    @Before
    fun setUp(){
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun loadGameList(){
        onView(withId(R.id.rvHome)).check(matches(isDisplayed())).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(dummy.size))
    }

}