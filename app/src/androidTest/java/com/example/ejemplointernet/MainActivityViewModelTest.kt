package com.example.ejemplointernet

import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.*


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityViewModelTest {

    private var viewModel = MainActivityViewModel()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun shouldEnableButtonTextTest() {
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

    @Test
    fun generateUrlPrivateTest() {
        runBlocking {
            val pairIdResult = arrayOf(
                Pair("1",  "https://swapi.dev/api/planets/1/"),
                Pair("2",  "https://swapi.dev/api/planets/2/"),
                Pair("3",  "https://swapi.dev/api/planets/3/"),
                Pair("4",  "https://swapi.dev/api/planets/4/"),
                Pair("5",  "https://swapi.dev/api/planets/5/"),
            )

            val method =
                viewModel.javaClass.getDeclaredMethod("generateUrlPrivate", String::class.java)
            method.isAccessible = true
            pairIdResult.forEach {
                val url = method.invoke(viewModel, it.first)
                Assert.assertEquals((url as String), it.second)
            }
        }
    }

    @Test
    fun generateUrlInternalTest() {
        runBlocking {
            val pairIdResult = arrayOf(
                Pair("1",  "https://swapi.dev/api/planets/1/"),
                Pair("2",  "https://swapi.dev/api/planets/2/"),
                Pair("3",  "https://swapi.dev/api/planets/3/"),
                Pair("4",  "https://swapi.dev/api/planets/4/"),
                Pair("5",  "https://swapi.dev/api/planets/5/"),
            )
            pairIdResult.forEach {
                Assert.assertEquals(viewModel.generateUrlInternal(it.first), it.second)
            }
        }
    }

    @Test
    fun sendGetPlanetRequestCallbacksTest() {
        runBlocking {
            viewModel.sendGetPlanetRequestCallbacks("1")
            // TODO This request is not completed. We aren't testing anything.
        }
    }

    @Test
    fun sendGetPlanetRequestSynchronous() {
        viewModel.planetReceived.observeForever {  }
        val pairIdResult = arrayOf(
            Pair("1",  "Tatooine"),
            Pair("2",  "Alderaan"),
            Pair("3",  "Yavin IV"),
            Pair("4",  "Hoth"),
            Pair("5",  "Dagobah"),
        )

        pairIdResult.forEach {
            runBlocking {
                viewModel.sendGetPlanetRequestSynchronous(it.first)
            }
            Assert.assertEquals(viewModel.planetReceived.value,it.second)
        }

    }

}