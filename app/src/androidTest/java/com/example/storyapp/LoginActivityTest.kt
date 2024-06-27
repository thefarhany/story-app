package com.example.storyapp

import androidx.compose.ui.test.isRoot
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.storyapp.ui.login.Login
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import com.example.storyapp.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {
    private val email = "ahmed12@gmail.com"
    private val password = "ahmed12345"

    @get:Rule
    val activity = ActivityScenarioRule(Login::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun login_logout_success() {
        //Espresso.onView(isRoot()).perform(ViewActions.waitFor(2000))

        Espresso.onView(withId(R.id.edtLoginEmail))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.edtLoginEmail)).perform(
            ViewActions.typeText(email),
            ViewActions.closeSoftKeyboard()
        )

        Espresso.onView(withId(R.id.edtLoginPassword))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.edtLoginPassword)).perform(
            ViewActions.typeText(password),
            ViewActions.closeSoftKeyboard()
        )

        Espresso.onView(withId(R.id.btnLogin))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.btnLogin)).perform(ViewActions.click())

        Espresso.onView(withId(R.id.rvStories))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(withId(R.id.action_logout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.action_logout)).perform(ViewActions.click())

        Espresso.onView(withText(R.string.onboarding_title))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}