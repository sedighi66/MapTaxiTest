package org.msfox.maptaxitest.binding

import androidx.databinding.DataBindingComponent
import androidx.fragment.app.Fragment
import javax.inject.Inject

/**
 * A Data Binding Component implementation for fragments.
 */
class FragmentDataBindingComponent @Inject constructor(fragment: Fragment) : DataBindingComponent {
    private val adapter = FragmentBindingAdapters(fragment)

    override fun getFragmentBindingAdapters() = adapter
}
