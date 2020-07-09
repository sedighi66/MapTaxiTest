package org.msfox.maptaxitest.binding

import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import org.msfox.maptaxitest.utils.OpenForTesting
import javax.inject.Inject

/**
 * Binding adapters that work with a fragment instance.
 */
@OpenForTesting
class FragmentBindingAdapters(private val fragment: Fragment) {
    @BindingAdapter(value = ["imageUrl"], requireAll = false)
    fun bindImage(imageView: ImageView, url: String?) {
        Glide.with(fragment).load(url).into(imageView)
    }
}

