package org.msfox.maptaxitest.ui

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.msfox.maptaxitest.R
import org.msfox.maptaxitest.binding.FragmentDataBindingComponent
import org.msfox.maptaxitest.databinding.MapFragmentBinding
import org.msfox.maptaxitest.di.Injectable
import org.msfox.maptaxitest.utils.GlideBitmapLoader
import org.msfox.maptaxitest.utils.autoCleared
import org.msfox.maptaxitest.vm.MapViewModel
import javax.inject.Inject

/**
 * Created by mohsen on 06,July,2020
 */
class MapFragment : Fragment(), Injectable, OnMapReadyCallback {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

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
    }


        override fun onMapReady(googleMap: GoogleMap?) {
            googleMap ?: return
            with(googleMap) {
                viewModel.getVehicles().observe(viewLifecycleOwner, Observer { vehicles ->
                    var first = true
                    vehicles.data?.forEach { vehicle ->
                        val latLng = LatLng(vehicle.lat, vehicle.lng)
                        //set camera to first vehicle.
                        //true way is to set camera to user location and show
                        //taxis around it.
                        if(first) {
                            moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL))
                            first = false
                        }
                        GlideBitmapLoader.load(requireContext(),
                            vehicle.imageUrl,
                            VEHICLE_SIZE,
                            VEHICLE_SIZE){ resource ->
                            val descriptor =
                                BitmapDescriptorFactory.fromBitmap(resource)

                            addMarker(MarkerOptions().position(latLng)).also {
                                it.setIcon(descriptor)
                                it.rotation = vehicle.bearing.toFloat()
                            }
                        }
                    }
                })
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

    companion object{
        private const val ZOOM_LEVEL = 16f
        private const val VEHICLE_SIZE = 140

    }

}
