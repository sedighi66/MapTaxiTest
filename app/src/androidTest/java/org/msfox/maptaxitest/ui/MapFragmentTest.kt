package org.msfox.maptaxitest.ui

import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.hamcrest.core.IsNull.notNullValue
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by mohsen on 14,July,2020
 */
@RunWith(AndroidJUnit4::class)
class MapFragmentTest{


    //for map tests, first of all we should change the app test runner to default in build.gradle.
    private lateinit var device: UiDevice

    @Before
    fun startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Start from the home screen
        device.pressHome()

        // Wait for launcher
        val launcherPackage: String = device.launcherPackageName
        assertThat(launcherPackage, notNullValue())
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT)

        // Launch the app
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(
            context.packageName)!!.apply {
            // Clear out any previous instances
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        context.startActivity(intent)

        // Wait for the app to appear
        device.wait(
            Until.hasObject(By.pkg(context.packageName).depth(0)), LAUNCH_TIMEOUT)
    }

    /**
     * first change the app test runner from gradle.
     */
    @Test
    fun vehiclesTitlesShouldBeDisplayedInMap() {
        val device = UiDevice.getInstance(Instrumentation())
        //Using UiAutomator, we does't have access to viewModel to get data to use here.
        //So we assumed that every vehicle type is ECO or PLUS. And put the vehicle type
        //to items title on the map. So for test, if we have one of these items on the map
        //test is passed.
        val markerExistence1 = device.findObject(UiSelector().descriptionContains("ECO")).exists()
        val markerExistence2 = device.findObject(UiSelector().descriptionContains("PLUS")).exists()

        Assert.assertTrue(markerExistence1 || markerExistence2)
    }

    companion object {
        private const val LAUNCH_TIMEOUT = 10000L
    }
}