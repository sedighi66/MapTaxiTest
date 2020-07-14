package org.msfox.maptaxitest.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

/**
 * Created by mohsen on 14,July,2020
 */
object GlideBitmapLoader {

    fun load(context: Context,
             url: String,
             bitmapWidth: Int = Int.MIN_VALUE,
             bitmapHeight: Int = Int.MIN_VALUE,
             resourceReadyCallback: ((Bitmap) -> Unit)
    ) {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>(bitmapWidth, bitmapHeight) {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    resourceReadyCallback.invoke(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }
}