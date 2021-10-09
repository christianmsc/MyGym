package com.github.christianmsc.com.github.christianmsc.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.github.christianmsc.com.github.christianmsc.data.database.entities.ExercisesEntity
import com.github.christianmsc.com.github.christianmsc.models.Exercise
import com.github.christianmsc.com.github.christianmsc.util.NetworkResult

class ExercisesBinding {

    companion object {


        @BindingAdapter("readApiResponse","readDatabase",requireAll = true)
        @JvmStatic
        fun handleReadDataErrors(
            view: View,
            apiResponse: NetworkResult<Exercise>?,
            database: List<ExercisesEntity>?
        ){
            when (view){
                is ImageView ->{
                    view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
                }
                is TextView ->{
                    view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
                    view.text = apiResponse?.message.toString()
                }
            }
        }

    }
}