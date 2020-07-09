package org.msfox.maptaxitest.utils

import android.app.Application

/**
 * We use a separate App for tests to prevent initializing dependency injection.
 *
 */
class TestApp : Application()
