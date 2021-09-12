package lufra.youpidapp

import android.view.Gravity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.NavigationViewActions.navigateTo
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(HomeUITest::class, ParametersUITest::class, AproposUITest::class)
class UIInstrumentedTestSuite {
    @get:Rule
        val activityRule = ActivityScenarioRule(MainActivity::class.java)

    companion object {
        fun changeOfFragmentFromDrawer(menuItemId: Int) {
            // Open Drawer to click on navigation
            onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.START)))
                .perform(DrawerActions.open())

            // Start the screen of your activity
            onView(withId(R.id.nav_view))
                .perform(navigateTo(menuItemId))
        }
    }

    @Test
    fun randomBtnClicked() {
        onView(withId(R.id.button_random)).perform(click()).check(matches(isDisplayed()))
    }

}
