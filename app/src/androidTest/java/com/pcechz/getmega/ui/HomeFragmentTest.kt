package com.pcechz.getmega.ui

import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.pcechz.getmega.R
import com.pcechz.getmega.data.model.ItemHolder
import com.pcechz.getmega.data.model.ItemHolder.Repo
import com.pcechz.getmega.data.model.TestRepoData
import com.pcechz.getmega.ui.adapter.RepoAdapter
import junit.framework.TestCase

import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class HomeFragmentTest {

    @get: Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)


    @Before
    fun init() {
        activityRule.activity.supportFragmentManager.beginTransaction()
    }

    /*
    * Toolbar text shows
    * */
    @Test
    fun title_is_trending() {
        onView(withId(R.id.toolbarText)).check(matches(isDisplayed()))
        onView(withId(R.id.toolbarText)).check(matches(withText("Trending")))
    }

    /*
  * Menu shows
  * */
    @Test
    fun menu_is_shows() {
        onView(withId(R.id.menu_toolbar)).check(matches(isDisplayed()))
    }

    /*
    * HomeFragment shows
    * */

    @Test
    fun check_fragment() {
        val fragment =
            activityRule.activity.supportFragmentManager.findFragmentByTag(HomeFragment.TAG)
        Assert.assertTrue(fragment is HomeFragment)
    }

    /*
    * RecyclerView shows
    * */

    @Test
    fun test_isHomeFragmentVisible_onAppLaunch(){
        onView(withId(R.id.nav_host_fragment_content_main)).check(matches(isDisplayed()))
    }

    /*
    * selected item, expand and collapse
    * isExpand and collapse?
    * */
    @Test
    fun test_selectItemIsExpanded() {

        Thread.sleep(2000);
        onView(withId(R.id.recycler)).perform(actionOnItemAtPosition<RepoAdapter.ViewHolder>(2, click()))
        // check if repo expanded
        onView(withId(R.id.recycler)).check(matches(checkExpandedItem(2)))
        // check any other repo should be collapsed
        onView(withId(R.id.recycler)).check(matches(CoreMatchers.not(checkExpandedItem(3))))

    }




    /*
    * checkExpandedItem
    * */
    private fun checkExpandedItem(pos: Int): Matcher<View> {
        return object :
            BoundedMatcher<View, View>(
                View::class.java
            ) {
            override fun describeTo(description: Description) {
                description.appendText("No item found")
            }

            override fun matchesSafely(view: View): Boolean {
                Assert.assertTrue("View is RecyclerView", view is RecyclerView)
                val recyclerView = view as RecyclerView
                val holder =
                    recyclerView.findViewHolderForAdapterPosition(pos) as RepoAdapter.ViewHolder
                val collapsedLayout =
                    holder.itemView.findViewById<RelativeLayout>(R.id.expanded_view)
                return collapsedLayout != null && collapsedLayout.visibility == View.VISIBLE
            }

        }
    }
}