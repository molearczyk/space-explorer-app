package com.molearczyk.spaceexplorer.ui.main


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.molearczyk.spaceexplorer.R
import com.molearczyk.spaceexplorer.explorationscreen.MainActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class BasicSearchFlowTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun basicSearchFlowTest() {
        waitForResults()
        val textInputEditText = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.searchInputLayout),
                                0),
                        0),
                        isDisplayed()))
        textInputEditText.perform(replaceText("moon"), closeSoftKeyboard())

        val textInputEditText2 = onView(
                allOf(withText("moon"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.searchInputLayout),
                                        0),
                                0),
                        isDisplayed()))
        textInputEditText2.perform(pressImeActionButton())

        val appCompatImageView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.spaceImagesRecyclerView),
                                childAtPosition(
                                        withClassName(`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                                        0)),
                        3),
                        isDisplayed()))
        appCompatImageView.perform(click())

        val appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withId(R.id.toolbarWrapperAppBarLayout),
                                                0)),
                                0),
                        isDisplayed()))
        appCompatImageButton.perform(click())
        waitForResults()
        onView(
                allOf(withId(R.id.text_input_end_icon), withContentDescription("Clear text"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.widget.FrameLayout")),
                                        2),
                                0),
                        isDisplayed()))
    }

    private fun waitForResults() {
        Thread.sleep(3000)
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
