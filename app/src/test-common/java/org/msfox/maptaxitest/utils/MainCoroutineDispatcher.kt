package org.msfox.maptaxitest.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.msfox.maptaxitest.AppCoroutineDispatchers

/**
 * Created by mohsen on 05,July,2020
 */
@ExperimentalCoroutinesApi
class MainCoroutineRule(
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    fun runBlockingTest(block: suspend () -> Unit) =
        this.testDispatcher.runBlockingTest {
            block()
        }

    fun getAppCoroutineDispatchers(): AppCoroutineDispatchers{
        return AppCoroutineDispatchers(testDispatcher,
        testDispatcher, testDispatcher)
    }
}