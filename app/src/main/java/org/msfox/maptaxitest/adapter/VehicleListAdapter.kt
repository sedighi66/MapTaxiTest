package org.msfox.maptaxitest.adapter

import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import org.msfox.maptaxitest.AppCoroutineDispatchers
import org.msfox.maptaxitest.R
import org.msfox.maptaxitest.databinding.VehicleItemBinding
import org.msfox.maptaxitest.model.Vehicle

/**
 * A RecyclerView adapter for [Vehicle] class.
 */
class VehicleListAdapter(
    appCoroutineDispatchers: AppCoroutineDispatchers,
    private val dataBindingComponent: DataBindingComponent,
    private val vehicleClickCallBack: ((Vehicle) -> Unit)?
) : DataBoundListAdapter<Vehicle, VehicleItemBinding>(
    appCoroutineDispatchers = appCoroutineDispatchers,
    diffCallback = object : DiffUtil.ItemCallback<Vehicle>() {
        override fun areItemsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
            return oldItem.lat == newItem.lat &&
            oldItem.lng == newItem.lng
        }
    }
) {

    override fun createBinding(parent: ViewGroup): VehicleItemBinding {

        val binding = DataBindingUtil.inflate<VehicleItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.vehicle_item,
            parent,
            false,
            dataBindingComponent
        )
        binding.root.setOnClickListener { _ ->
            binding.vehicle.let {
                if(it != null) vehicleClickCallBack?.invoke(it)
            }
        }

        return binding
    }

    override fun bind(binding: VehicleItemBinding, item: Vehicle) {
        binding.vehicle = item
    }
}
