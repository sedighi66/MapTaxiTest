package org.msfox.maptaxitest.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import org.msfox.maptaxitest.AppCoroutineDispatchers
import org.msfox.maptaxitest.R
import org.msfox.maptaxitest.adapter.VehicleListAdapter
import org.msfox.maptaxitest.binding.FragmentDataBindingComponent
import org.msfox.maptaxitest.databinding.ListFragmentBinding
import org.msfox.maptaxitest.di.Injectable
import org.msfox.maptaxitest.repository.Status
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

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

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
            false,
            dataBindingComponent
        )
        binding.status = Status.LOADING
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        adapter = VehicleListAdapter(appCoroutineDispatchers, dataBindingComponent){vehicle ->
            //for the future, if we want to add some behaviour regarding clicking items in recyclerview
        //    Toast.makeText(context, vehicle.toString(), Toast.LENGTH_LONG).show()
        }

        binding.vehicleList.adapter = adapter

        initVehicleList()
    }

    private fun initVehicleList() {
        viewModel.getVehicles().observe(viewLifecycleOwner, Observer { vehicles ->
            adapter.submitList(vehicles?.data)
            binding.status = vehicles?.status
        })
    }

}
