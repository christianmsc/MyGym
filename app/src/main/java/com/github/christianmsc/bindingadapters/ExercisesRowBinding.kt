package com.github.christianmsc.com.github.christianmsc.bindingadapters

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.github.christianmsc.R
import com.github.christianmsc.com.github.christianmsc.models.ExerciseItem
import com.github.christianmsc.com.github.christianmsc.ui.fragments.exercises.ExercisesFragmentDirections
import java.lang.Exception

class ExercisesRowBinding {

    companion object {
        @BindingAdapter("onExerciseClickListener")
        @JvmStatic
        fun onExerciseClickListener(exerciseRowLayout: ConstraintLayout, result: ExerciseItem){
            exerciseRowLayout.setOnClickListener{
                try {
                    val action =
                        ExercisesFragmentDirections.actionExercisesFragmentToDetailsActivity(result)
                    exerciseRowLayout.findNavController().navigate(action)
                }catch (e:Exception){
                    Log.d("onExerciseClickListener", e.toString())
                }
            }
        }

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