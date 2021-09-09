package lufra.youpidapp

import android.view.Gravity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.LayoutAssertions.noEllipsizedText
import androidx.test.espresso.assertion.LayoutAssertions.noOverlaps
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyAbove
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyBelow
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.contrib.DrawerActions.*
import androidx.test.espresso.contrib.DrawerMatchers.*
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class UIInstrumentedTest {
    @get:Rule
        val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun randomBtnClicked() {
        onView(withId(R.id.button_random)).perform(click()).check(matches(isDisplayed()))
    }

    @Test
    fun clickParameters() {
        // Open Drawer to click on navigation
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.START)))
            .perform(DrawerActions.open())

        // Start the screen of your activity
        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(3))

        // Check the root layouts first
        onView(withId(R.id.scrollview))
            .check(matches(hasChildCount(1)))
            .check(noOverlaps())
            .check(noEllipsizedText())

        onView(withId(R.id.constraint_layout))
            .check(noOverlaps())
            .check(noEllipsizedText())

        // Check each Element one by one
        val titleStr = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.parameters)

        onView(withId(R.id.title))
            .check(matches(isDisplayed()))
            .check(matches(isNotClickable()))
            .check(matches(not(hasContentDescription())))
            .check(matches(not(hasLinks())))
            .check(matches(isNotFocusable()))
            .check(matches(withText(titleStr)))
            .check(matches(hasTextColor(R.color.black)))
            .check(matches(CustomMatchers.withNormalStyle()))
            .check(matches(CustomMatchers.withNormalStyle()))
            .check(isCompletelyAbove(withId(R.id.type_of_reading)))

        val typeOfReadingStr = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.type)

        onView(withId(R.id.type_of_reading))
            .check(matches(isDisplayed()))
            .check(matches(isNotClickable()))
            .check(matches(not(hasContentDescription())))
            .check(matches(withText(typeOfReadingStr)))
            .check(matches(hasTextColor(R.color.black)))
            .check(matches(CustomMatchers.withNormalStyle()))

        // Check the spinner

        var tc = InstrumentationRegistry.getInstrumentation().targetContext

        val type1 = tc.getString(R.string.type1)
        val type2 = tc.getString(R.string.type2)
        val type3 = tc.getString(R.string.type3)
        val type4 = tc.getString(R.string.type4)

        onView(withId(R.id.type_spinner))
            .check(matches(isDisplayed()))
            .check(matches(isClickable()))
            //.check(matches(withSpinnerText(containsString(type2))))  depends des prefs
            .check(isCompletelyBelow(withId(R.id.title)))
            .perform(click())

        onData(anything()).atPosition(1).perform(click())

        onView(withId(R.id.type_spinner))
            .check(matches(withSpinnerText(containsString(type2))))
            .perform(click())

        onData(anything()).atPosition(2).perform(click())

        onView(withId(R.id.type_spinner))
            .check(matches(withSpinnerText(containsString(type3))))
            .perform(click())

        onData(anything()).atPosition(3).perform(click())

        onView(withId(R.id.type_spinner))
            .check(matches(withSpinnerText(containsString(type4))))
            .perform(click())

        onData(anything()).atPosition(0).perform(click())

        onView(withId(R.id.type_spinner))
            .check(matches(withSpinnerText(containsString(type1))))

        // Check the checkbox

        tc = InstrumentationRegistry.getInstrumentation().targetContext

        val checkboxText = tc.getString(R.string.open_app_on_favorites)

        onView(withId(R.id.checkbox_open_on_favorites))
            .check(matches(isDisplayed()))
            .check(matches(withText(checkboxText)))
            .check(matches(isNotChecked()))
            .check(isCompletelyBelow(withId(R.id.title)))
            .perform(click())
            .check(matches(isChecked()))
            .perform(click())
            .check(matches(isNotChecked()))

        val pitchStr = tc.getString(R.string.pitch)

        onView(withId(R.id.pitch))
            .check(matches(isDisplayed()))
            .check(matches(withText(pitchStr)))
            .check(matches(CustomMatchers.withNormalStyle()))
            .check(matches(isNotFocusable()))
            .check(matches(isNotClickable()))

        val SEEKBAR_PROGRESS_DEFAULT = 10

        onView(withId(R.id.pitch_seekBar))
            .check(matches(isDisplayed()))
            .check(matches(isFocusable()))
            .check(matches(isNotClickable()))
            .check(matches(CustomMatchers.withSeekbarProgress(SEEKBAR_PROGRESS_DEFAULT)))
            .perform(CustomViewActions.setProgress(0))
            .check(matches(CustomMatchers.withSeekbarProgress(0)))

        // Test the default button behavior

        onView(withId(R.id.default_settings_button))
            .check(matches(isDisplayed()))
            .check(matches(isFocusable()))
            .check(matches(isClickable()))
            .perform(click())

        onView(withId(R.id.pitch_seekBar))
            .check(matches(CustomMatchers.withSeekbarProgress(SEEKBAR_PROGRESS_DEFAULT)))
        onView(withId(R.id.checkbox_open_on_favorites))
            .check(matches(isNotChecked()))
        onView(withId(R.id.type_spinner))
            .check(matches(withSpinnerText(containsString(type1))))
    }
}