package com.test.fitnessstudios.detail

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.squareup.picasso.Picasso

class DetailViewModel : ViewModel() {
    // TODO: Implement the ViewModel
}

@BindingAdapter("imageUrl")
fun loadImage(view : View, url : String?){
    if (url.isNullOrEmpty()) return
    Picasso
        .get()
        .load(url)
        .into((view as ImageView))
}