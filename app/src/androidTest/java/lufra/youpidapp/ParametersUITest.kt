package lufra.youpidapp

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.LayoutAssertions.noEllipsizedText
import androidx.test.espresso.assertion.LayoutAssertions.noOverlaps
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyAbove
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyBelow
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matchers
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class ParametersUITest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    val SEEKBAR_PROGRESS_DEFAULT = 10
    val PLAYMODE_DEFAULT = 0

    @Before
    fun setup() {
        UIInstrumentedTestSuite.changeOfFragmentFromDrawer(3)

        onView(withId(R.id.pitch_seekBar))
            .perform(CustomViewActions.setProgress(SEEKBAR_PROGRESS_DEFAULT))
        onView(withId(R.id.type_spinner))
            .perform(click())
        onData(Matchers.anything()).atPosition(PLAYMODE_DEFAULT).perform(click())
        onView(withId(R.id.checkbox_open_on_favorites)).perform(CustomViewActions.setChecked(false))
    }

    @Test
    fun staticElementsTest() {
        val toolbarTitle = InstrumentationRegistry.getInstrumentation().targetContext
            .getString(R.string.app_name)
        onView(withId(R.id.my_toolbar))
            .check(matches(CustomMatchers.withToolbarTitle(toolbarTitle)))
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
            .check(isCompletelyAbove(withId(R.id.type_of_reading)))

        val typeOfReadingStr = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.type)

        onView(withId(R.id.type_of_reading))
            .check(matches(isDisplayed()))
            .check(matches(isNotClickable()))
            .check(matches(not(hasContentDescription())))
            .check(matches(withText(typeOfReadingStr)))
            .check(matches(hasTextColor(R.color.black)))
            .check(matches(CustomMatchers.withNormalStyle()))
    }

    @Test
    fun playModesTest() {
//        UIInstrumentedTest.changeOfFragmentFromDrawer(3)

        val tc = InstrumentationRegistry.getInstrumentation().targetContext

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

        onData(Matchers.anything()).atPosition(1).perform(click())

        onView(withId(R.id.type_spinner))
            .check(matches(withSpinnerText(Matchers.containsString(type2))))
            .perform(click())

        onData(Matchers.anything()).atPosition(2).perform(click())

        onView(withId(R.id.type_spinner))
            .check(matches(withSpinnerText(Matchers.containsString(type3))))
            .perform(click())

        onData(Matchers.anything()).atPosition(3).perform(click())

        onView(withId(R.id.type_spinner))
            .check(matches(withSpinnerText(Matchers.containsString(type4))))
            .perform(click())

        onData(Matchers.anything()).atPosition(0).perform(click())

        onView(withId(R.id.type_spinner))
            .check(matches(withSpinnerText(Matchers.containsString(type1))))

        val pitchStr = tc.getString(R.string.pitch)

        onView(withId(R.id.pitch))
            .check(matches(isDisplayed()))
            .check(matches(withText(pitchStr)))
            .check(matches(CustomMatchers.withNormalStyle()))
            .check(matches(isNotFocusable()))
            .check(matches(isNotClickable()))
    }

    @Test
    fun openOnFavoriteOptionTest() {
        val tc = InstrumentationRegistry.getInstrumentation().targetContext

        val checkboxText = tc.getString(R.string.open_app_on_favorites)

        onView(withId(R.id.checkbox_open_on_favorites))
            .check(matches(isDisplayed()))
            .check(matches(withText(checkboxText)))
            .check(matches(isNotChecked()))
            .check(isCompletelyBelow(withId(R.id.title)))
            .perform(click())
            .check(matches(isChecked()))
    }

    @Test
    fun changePitchTest() {
        onView(withId(R.id.pitch_seekBar))
            .check(matches(isDisplayed()))
            .check(matches(isFocusable()))
            .check(matches(isNotClickable()))
            .check(matches(CustomMatchers.withSeekbarProgress(SEEKBAR_PROGRESS_DEFAULT)))
            .perform(CustomViewActions.setProgress(0))
            .check(matches(CustomMatchers.withSeekbarProgress(0)))
    }

    @Test
    fun defaultBtnNothingHasChangedTest() {
        // Scenario 1 - the user just press the default button without touching anything else

        onView(withId(R.id.default_settings_button))
            .check(matches(isDisplayed()))
            .check(matches(isFocusable()))
            .check(matches(isClickable()))
            .perform(click())

        onView(withId(R.id.pitch_seekBar))
            .check(matches(CustomMatchers.withSeekbarProgress(SEEKBAR_PROGRESS_DEFAULT)))
        onView(withId(R.id.checkbox_open_on_favorites))
            .check(matches(isNotChecked()))
        val type1 = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.type1)
        onView(withId(R.id.type_spinner))
            .check(matches(withSpinnerText(Matchers.containsString(type1))))
    }

    @Test
    fun defaultBtnEveryValueChangedTest() {
        // Scenario 2 - we change every parameters out of their default value and
        //              press the reset button, which only change the pitch seekbar

        val tc = InstrumentationRegistry.getInstrumentation().targetContext
        val type1 = tc.getString(R.string.type1)
        val type2 = tc.getString(R.string.type2)

        // change the values
        onView(withId(R.id.pitch_seekBar))
            .check(matches(CustomMatchers.withSeekbarProgress(SEEKBAR_PROGRESS_DEFAULT)))
            .perform(CustomViewActions.setProgress(20))
        onView(withId(R.id.checkbox_open_on_favorites))
            .check(matches(isNotChecked()))
            .perform(click())
            .check(matches(isChecked()))
        onView(withId(R.id.type_spinner))
            .check(matches(withSpinnerText(Matchers.containsString(type1))))
            .perform(click())
        onData(Matchers.anything()).atPosition(1).perform(click())
        onView(withId(R.id.type_spinner))
            .check(matches(withSpinnerText(Matchers.containsString(type2))))

        // press the default button
        onView(withId(R.id.default_settings_button)).perform(click())

        // check the other buttons
        onView(withId(R.id.pitch_seekBar))
            .check(matches(CustomMatchers.withSeekbarProgress(SEEKBAR_PROGRESS_DEFAULT)))
        onView(withId(R.id.checkbox_open_on_favorites)).check(matches(isChecked()))
        onView(withId(R.id.type_spinner))
            .check(matches(withSpinnerText(Matchers.containsString(type2))))
    }
}