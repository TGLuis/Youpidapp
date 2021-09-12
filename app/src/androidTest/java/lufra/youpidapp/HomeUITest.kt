package lufra.youpidapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import lufra.youpidapp.CustomMatchers.withNbOfMenuItemInToolbar
import lufra.youpidapp.CustomMatchers.withToolbarTitle
import lufra.youpidapp.CustomViewActions.clickToolbarItem
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class HomeUITest {
    @get:Rule
        val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun randomBtnClicked() {
        onView(withId(R.id.button_random))
            .perform(click())
            .check(matches(isDisplayed()))
            .check(matches(isClickable()))
            .check(matches(isFocusable()))
            .check(matches(withText(R.string.random)))
    }

    @Test
    fun actionBarTest() {
        val tc = InstrumentationRegistry.getInstrumentation().targetContext
        val toolbarTitle = tc.getString(R.string.app_name)
        onView(withId(R.id.my_toolbar))
            .check(matches(withToolbarTitle(toolbarTitle)))
            .check(matches(withNbOfMenuItemInToolbar(3)))
    }

    @Test
    fun actionBarSearchTest() {
        val tc = InstrumentationRegistry.getInstrumentation().targetContext
        onView(withId(R.id.my_toolbar))
            .perform(clickToolbarItem(0))

    }

    @Test
    fun actionBarPauseTest() {
        val tc = InstrumentationRegistry.getInstrumentation().targetContext
        onView(withId(R.id.my_toolbar))
            .perform(clickToolbarItem(1))
    }

    @Test
    fun actionBarOptionsTest() {
        val tc = InstrumentationRegistry.getInstrumentation().targetContext
        onView(withId(R.id.my_toolbar))
            .perform(clickToolbarItem(2))
    }
}