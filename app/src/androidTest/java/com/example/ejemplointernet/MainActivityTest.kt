package com.example.ejemplointernet

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)

    @get:Rule
    var activityScenarioRule = activityScenarioRule<MainActivity>(intent)

    @Test
    fun checkButtonStatusAccordingToEditText(){
        Espresso.onView(withId(R.id.b_descarga))
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isEnabled())))

        Espresso.onView(withId(R.id.et_number))
            .perform(ViewActions.typeText("Something"), ViewActions.closeSoftKeyboard())

        Espresso.onView(withId(R.id.b_descarga))
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isEnabled())))

        Espresso.onView(withId(R.id.et_number))
            .perform(ViewActions.typeText("1.1"), ViewActions.closeSoftKeyboard())

        Espresso.onView(withId(R.id.b_descarga))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))

        Espresso.onView(withId(R.id.et_number))
            .check(ViewAssertions.matches(ViewMatchers.withText("11")))

        Espresso.onView(withId(R.id.et_number)).perform(ViewActions.clearText())

        Espresso.onView(withId(R.id.et_number))
            .perform(ViewActions.typeText("2,2"), ViewActions.closeSoftKeyboard())

        Espresso.onView(withId(R.id.b_descarga))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))

        Espresso.onView(withId(R.id.et_number))
            .check(ViewAssertions.matches(ViewMatchers.withText("22")))

        Espresso.onView(withId(R.id.et_number)).perform(ViewActions.clearText())

        Espresso.onView(withId(R.id.et_number))
            .perform(ViewActions.typeText("1"), ViewActions.closeSoftKeyboard())

        Espresso.onView(withId(R.id.b_descarga))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))

    }
}