package org.msfox.maptaxitest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.msfox.maptaxitest.AppCoroutineDispatchers
import org.msfox.maptaxitest.R
import org.msfox.maptaxitest.adapter.VehicleListAdapter
import org.msfox.maptaxitest.databinding.ListFragmentBinding
import org.msfox.maptaxitest.di.Injectable
import org.msfox.maptaxitest.utils.autoCleared
import org.msfox.maptaxitest.vm.VehicleListViewModel
import javax.inject.Inject

/**
 * Created by mohsen on 06,July,2020
 */
class VehicleListFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var appCoroutineDispatchers: AppCoroutineDispatchers

    var binding by autoCleared<ListFragmentBinding>()

    var adapter by autoCleared<VehicleListAdapter>()

    private val viewModel: VehicleListViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.list_fragment,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        adapter = VehicleListAdapter(appCoroutineDispatchers)

        binding.vehicleList.adapter = adapter

        initVehicleList()
    }

    private fun initVehicleList() {
        viewModel.getVehicles(false).observe(viewLifecycleOwner, Observer { vehicles ->
            adapter.submitList(vehicles?.data)
        })
    }

}
