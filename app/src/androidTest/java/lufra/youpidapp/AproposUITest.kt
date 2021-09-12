package lufra.youpidapp

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.LayoutAssertions.noEllipsizedText
import androidx.test.espresso.assertion.LayoutAssertions.noOverlaps
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyAbove
import androidx.test.espresso.assertion.PositionAssertions.isCompletelyBelow
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.matcher.UriMatchers.hasPath
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.LayoutMatchers.hasMultilineText
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import lufra.youpidapp.UIInstrumentedTestSuite.Companion.changeOfFragmentFromDrawer
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers
import org.hamcrest.Matchers.oneOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class AproposUITest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val intentsTestRule = IntentsTestRule(MainActivity::class.java)

    @Before
    fun setup() {
        changeOfFragmentFromDrawer(5)
    }

    @Test
    fun corpsTest() {
        val toolbarTitle = InstrumentationRegistry.getInstrumentation().targetContext
            .getString(R.string.app_name)
        onView(withId(R.id.my_toolbar))
            .check(matches(CustomMatchers.withToolbarTitle(toolbarTitle)))

        Espresso.onView(withId(R.id.constraint_layout))
            .check(noOverlaps())
            .check(noEllipsizedText())

        val titleStr = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.about_title)

        onView(withId(R.id.title))
            .check(matches(isCompletelyDisplayed()))
            .check(matches(isNotClickable()))
            .check(matches(not(hasContentDescription())))
            .check(matches(not(hasLinks())))
            .check(matches(isNotFocusable()))
            .check(matches(withText(titleStr)))
            .check(matches(hasTextColor(R.color.black)))
            .check(matches(CustomMatchers.withBoldStyle()))
            .check(isCompletelyAbove(withId(R.id.corps)))

        // Checks the corps of text

        onView(withId(R.id.corps))
            .check(matches(hasChildCount(6)))

        val corps1Str = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.about_corps_1)

        onView(withId(R.id.corps_1))
            .check(matches(isDisplayingAtLeast(10)))
            .check(matches(isNotClickable()))
            .check(matches(withText(corps1Str)))
            .check(matches(CustomMatchers.withNormalStyle()))
            .check(matches(hasMultilineText()))

        val corps2Str = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.about_corps_2)

        onView(withId(R.id.corps_2))
            .perform(scrollTo(), click())
            .check(matches(isDisplayed()))
            .check(matches(withText(corps2Str)))
            .check(matches(CustomMatchers.withNormalStyle()))
            .check(matches(hasMultilineText()))

        val corps3Str = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.about_corps_3)

        onView(withId(R.id.corps_3))
            .perform(scrollTo(), click())
            .check(matches(isDisplayed()))
            .check(matches(withText(corps3Str)))
            .check(matches(CustomMatchers.withNormalStyle()))
            //.check(matches(hasMultilineText()))  on smaller resolution yes, on bigger ones no

        val corps4Str = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.about_corps_4)

        onView(withId(R.id.corps_4))
            .perform(scrollTo(), click())
            .check(matches(isDisplayed()))
            .check(matches(withText(corps4Str)))
            .check(matches(CustomMatchers.withNormalStyle()))
            .check(matches(hasMultilineText()))

        val corps5Str = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.about_corps_5)

        onView(withId(R.id.corps_5))
            .perform(scrollTo(), click())
            .check(matches(isDisplayed()))
            .check(matches(withText(corps5Str)))
            .check(matches(CustomMatchers.withNormalStyle()))
            .check(matches(hasMultilineText()))

        val corps6Str = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.about_corps_6)

        onView(withId(R.id.corps_6))
            .perform(scrollTo(), click())
            .check(matches(isDisplayed()))
            .check(matches(withText(corps6Str)))
            .check(matches(CustomMatchers.withNormalStyle()))
            .check(matches(not(hasMultilineText())))
    }

    @Test
    fun githubFabTest() {
        // Checks the buttons below

        onView(withId(R.id.github_link))
            .perform(scrollTo())
            .check(matches(isDisplayed()))
            .check(matches(hasContentDescription()))
            .check(matches(withContentDescription(R.string.content_descr_github_fab)))
            .check(matches(isClickable()))
            .check(matches(isFocusable()))
            .check(isCompletelyBelow(withId(R.id.title)))
            .perform(click())

        Intents.intending(
            Matchers.allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(hasPath("https://github.com/TGLuis/Youpidapp"))
            )
        )
    }

    @Test
    fun youtubeFabTest() {
        onView(withId(R.id.youtube_link))
            .perform(scrollTo())
            .check(matches(isDisplayed()))
            .check(matches(hasContentDescription()))
            .check(matches(withContentDescription(R.string.content_descr_youtube_fab)))
            .check(matches(isClickable()))
            .check(matches(isFocusable()))
            .check(isCompletelyBelow(withId(R.id.title)))
            .perform(click())

        Intents.intending(
            Matchers.allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(hasPath("https://www.youtube.com/channel/UC-QAurzK1czAlnMFOqkfxfw"))
            )
        )
    }

    @Test
    fun covidImageViewTest() {
        onView(withId(R.id.covid))
            .perform(scrollTo())
            .check(matches(isDisplayed()))
            .check(matches(hasContentDescription()))
            .check(matches(withContentDescription(R.string.content_descr_covid)))
            .check(matches(isNotClickable()))
            .check(matches(isNotFocusable()))
            .check(matches(CustomMatchers.withDrawable(R.drawable.covid)))
            .check(isCompletelyBelow(withId(R.id.title)))
            .perform(click())

        Intents.intending(
            Matchers.allOf(
                hasAction(Intent.ACTION_VIEW),
                oneOf(
                    hasData(hasPath("https://youtu.be/miO-FRvDsfs")),
                    hasData(hasPath("https://youtu.be/AtCStX0RurY")),
                    hasData(hasPath("https://youtu.be/exGxhhHS0UE")),
                    hasData(hasPath("https://youtu.be/gAx8znBVux0")),
                    hasData(hasPath("https://youtu.be/rKFG5XcTaVk")),
                    hasData(hasPath("https://youtu.be/bvv2o4YuAwA")),
                    hasData(hasPath("https://youtu.be/rsvvCrKHSnY")),
                    hasData(hasPath("https://youtu.be/EkybbNppEvE")),
                    hasData(hasPath("https://youtu.be/LkjZFRNK9xo")),
                    hasData(hasPath("https://youtu.be/bxobXTOb6fM")),
                    hasData(hasPath("https://youtu.be/xfGsCLoN4Fg")),
                    hasData(hasPath("https://youtu.be/6EGAeQi474o")),
                    hasData(hasPath("https://youtu.be/oDJ6pZn7Y-g")),
                    hasData(hasPath("https://youtu.be/qVdh3T8ffIY")),
                    hasData(hasPath("https://youtu.be/ri8Jui3KEXg")),
                    hasData(hasPath("https://youtu.be/PEV4ChQ79Yw")),
                )
            )
        )
    }
}