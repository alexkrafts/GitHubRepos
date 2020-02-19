package com.test.githubrepos.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("imageSource")
    fun ImageView.setImageSource(url: String?) {
        url.let { Picasso.get().load(it).into(this) }
    }
}
