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
class MainActivityViewModelTest {

    private var viewModel = MainActivityViewModel()

    @Test
    fun shouldEnableButtonText() {
        runBlocking {
            val invalidText = arrayOf(
                "",
                "a",
                "1.1",
                "1,1",
                "-1"
            )
            invalidText.forEach { text ->
                viewModel.shouldEnableButton(text)
                viewModel.buttonEnabled.value?.let { enabled ->
                    assert(!enabled)
                }
            }

            val validText = arrayOf(
                "1",
                "99",
                "784"
            )
            validText.forEach { text ->
                viewModel.shouldEnableButton(text)
                viewModel.buttonEnabled.value?.let { enabled ->
                    assert(enabled)
                }
            }
        }

    }
}