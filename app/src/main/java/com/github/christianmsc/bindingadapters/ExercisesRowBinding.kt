package com.github.christianmsc.com.github.christianmsc.bindingadapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.github.christianmsc.R

class ExercisesRowBinding {

    companion object {

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadGifFromUrl(imageView: ImageView, imageUrl: String){
            imageView.load(imageUrl) {
                crossfade(durationMillis = 600)
                error(R.drawable.ic_error_placeholder)
            }
        }

        @BindingAdapter("target", "bodyPart", requireAll = true)
        @JvmStatic
        fun setDescriptionText(textView: TextView, target: String, bodyPart: String){
            textView.text = "Target: $target. Body Part: $bodyPart"
        }
    }
}