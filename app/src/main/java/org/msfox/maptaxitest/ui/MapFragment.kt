package org.msfox.maptaxitest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.msfox.maptaxitest.AppCoroutineDispatchers
import org.msfox.maptaxitest.R
import org.msfox.maptaxitest.binding.FragmentDataBindingComponent
import org.msfox.maptaxitest.databinding.MapFragmentBinding
import org.msfox.maptaxitest.di.Injectable
import org.msfox.maptaxitest.utils.autoCleared
import org.msfox.maptaxitest.vm.MapViewModel
import javax.inject.Inject

/**
 * Created by mohsen on 06,July,2020
 */
class MapFragment : Fragment(), Injectable, OnMapReadyCallback {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var appCoroutineDispatchers: AppCoroutineDispatchers

    var binding by autoCleared<MapFragmentBinding>()

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private val viewModel: MapViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.map_fragment,
            container,
            false,
            dataBindingComponent
        )
        binding.map.onCreate(savedInstanceState)

        binding.map.getMapAsync(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        initVehicleList()


    }

    private fun initVehicleList() {
        viewModel.getVehicles().observe(viewLifecycleOwner, Observer { vehicles ->

        })
    }



        val SYDNEY = LatLng(-33.862, 151.21)
        val ZOOM_LEVEL = 15f

        /**
         * This is where we can add markers or lines, add listeners or move the camera. In this case,
         * we just move the camera to Sydney and add a marker in Sydney.
         */
        override fun onMapReady(googleMap: GoogleMap?) {
            googleMap ?: return
            with(googleMap) {
                moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY, ZOOM_LEVEL))
                addMarker(MarkerOptions().position(SYDNEY))
            }
        }

    override fun onStart() {
        super.onStart()
        binding.map.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onStop() {
        super.onStop()
        binding.map.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.map.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.map.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.map.onLowMemory()
    }

}
