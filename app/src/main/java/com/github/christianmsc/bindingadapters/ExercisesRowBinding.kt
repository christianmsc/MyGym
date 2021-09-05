package com.github.christianmsc.com.github.christianmsc.bindingadapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load

class ExercisesRowBinding {

    companion object {

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadGifFromUrl(imageView: ImageView, imageUrl: String){
            imageView.load(imageUrl) {
                crossfade(durationMillis = 600)
            }
        }

        @BindingAdapter(value = ["target", "bodyPart"], requireAll = true)
        @JvmStatic
        fun setDescriptionText(textView: TextView, target: String, bodyPart: String){
            textView.text = "Target: $target. Body Part: $bodyPart"
        }
    }
}