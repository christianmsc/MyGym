package com.github.christianmsc.com.github.christianmsc.ui.fragments.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import coil.load
import com.bumptech.glide.Glide
import com.github.christianmsc.R
import com.github.christianmsc.com.github.christianmsc.models.ExerciseItem
import kotlinx.android.synthetic.main.fragment_overview.view.*


class OverviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_overview, container, false)

        val args = arguments
        val myBundle: ExerciseItem? = args?.getParcelable("exerciseBundle")

        Glide.with(view).load(myBundle?.gifUrl).into(view.main_imageView);
        view.title_textView.text = myBundle?.name
        ("\nEquipment: " + myBundle?.equipment + "\nTarget: " + myBundle?.target).also { view.summary_textView.text = it }

        when (myBundle?.bodyPart) {
            getString(R.string.back) -> {
                view.back_imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                view.back_textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            }
            getString(R.string.cardio) -> {
                view.cardio_imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                view.cardio_textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            }
            getString(R.string.chest) -> {
                view.chest_imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                view.chest_textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            }
            getString(R.string.lower_arms) -> {
                view.lowerArms_imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                view.lowerArms_textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            }
            getString(R.string.lower_legs) -> {
                view.lowerLegs_imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                view.lowerLegs_textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            }
            getString(R.string.neck) -> {
                view.neck_imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                view.neck_textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            }
            getString(R.string.shoulders) -> {
                view.shoulders_imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                view.shoulders_textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            }
            getString(R.string.upper_arms) -> {
                view.upperArms_imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                view.upperArms_textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            }
            getString(R.string.upper_legs) -> {
                view.upperLegs_imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                view.upperLegs_textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            }
            getString(R.string.waist) -> {
                view.waist_imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                view.waist_textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            }
        }

        return view
    }
}