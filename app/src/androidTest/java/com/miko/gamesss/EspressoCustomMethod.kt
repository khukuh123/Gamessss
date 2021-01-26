package com.miko.gamesss

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.miko.gamesss.core.domain.model.Section
import com.miko.gamesss.core.domain.model.SectionItem
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher

object EspressoCustomMethod {

    fun withCtlTitle(title: String): Matcher<Any?> {
        return withCtlTitle(equalTo(title))
    }

    fun withCtlTitle(title: Matcher<String>): Matcher<Any?> {
        return object :
            BoundedMatcher<Any?, CollapsingToolbarLayout>(CollapsingToolbarLayout::class.java) {
            override fun describeTo(description: Description?) {
                title.describeTo(description)
            }

            override fun matchesSafely(item: CollapsingToolbarLayout?): Boolean {
                return title.matches(item?.title)
            }

        }
    }

    fun withChildName(name: String?): Matcher<Any?> {
        return withChildName(equalTo(name))
    }

    fun withChildName(name: Matcher<String?>): Matcher<Any?> {
        return object : BoundedMatcher<Any?, Section>(Section::class.java) {
            var list = mutableListOf<SectionItem>()

            override fun matchesSafely(childStruct: Section): Boolean {
                var found = false
                for (i in childStruct.listItem) {
                    if (name.matches(i.name) && !list.contains(i)) {
                        found = true
                        list.add(i)
                        break
                    }
                }
                return found
            }

            override fun describeTo(description: Description) {
                name.describeTo(description)
            }
        }
    }

    fun clickViewInViewHolderWithId(id: Int): ViewAction {
        return object : ViewAction {
            private var click = ViewActions.click()

            override fun getDescription(): String {
                return "to click image in ViewHolder with id"
            }

            override fun getConstraints(): Matcher<View> {
                return this.click.constraints
            }

            override fun perform(uiController: UiController?, view: View?) {
                click.perform(uiController, view?.findViewById(id))
            }
        }
    }

    fun typeSearchViewText(text: String): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "Change SearchView text"
            }

            override fun getConstraints(): Matcher<View> {
                return Matchers.allOf(
                    ViewMatchers.isDisplayed(),
                    ViewMatchers.isAssignableFrom(SearchView::class.java)
                )
            }

            override fun perform(uiController: UiController?, view: View?) {
                (view as SearchView).setQuery(text, true)
            }
        }
    }

    fun withIndex(matcher: Matcher<View?>, index: Int): Matcher<View?> {
        return object : TypeSafeMatcher<View?>() {
            var currentIndex = 0
            var viewObjHash = 0
            override fun describeTo(description: Description) {
                description.appendText("with index: ")
                description.appendValue(index)
                matcher.describeTo(description)
            }

            override fun matchesSafely(view: View?): Boolean {
                if (matcher.matches(view) && currentIndex++ == index) {
                    viewObjHash = view.hashCode()
                }
                return viewObjHash == view.hashCode()
            }
        }
    }

}