package com.github.christianmsc.com.github.christianmsc.ui.fragments.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.github.christianmsc.R
import com.github.christianmsc.com.github.christianmsc.models.ExerciseItem
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.EXERCISE_RESULT_KEY
import com.github.christianmsc.databinding.FragmentOverviewBinding


class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: ExerciseItem = args!!.getParcelable<ExerciseItem>(EXERCISE_RESULT_KEY) as ExerciseItem

        Glide.with(binding.root).load(myBundle.gifUrl).into(binding.mainImageView);
        binding.titleTextView.text = myBundle.name
        ("\nEquipment: " + myBundle.equipment + "\nTarget: " + myBundle.target).also { binding.summaryTextView.text = it }

        updateColors(myBundle.bodyPart == getString(R.string.back), binding.backTextView, binding.backImageView)
        updateColors(myBundle.bodyPart == getString(R.string.cardio), binding.cardioTextView, binding.cardioImageView)
        updateColors(myBundle.bodyPart == getString(R.string.chest), binding.chestTextView, binding.chestImageView)
        updateColors(myBundle.bodyPart == getString(R.string.lower_arms), binding.lowerArmsTextView, binding.lowerArmsImageView)
        updateColors(myBundle.bodyPart == getString(R.string.lower_legs), binding.lowerLegsTextView, binding.lowerLegsImageView)
        updateColors(myBundle.bodyPart == getString(R.string.neck), binding.neckTextView, binding.neckImageView)
        updateColors(myBundle.bodyPart == getString(R.string.shoulders), binding.shouldersTextView, binding.shouldersImageView)
        updateColors(myBundle.bodyPart == getString(R.string.upper_arms), binding.upperArmsTextView, binding.upperArmsImageView)
        updateColors(myBundle.bodyPart == getString(R.string.upper_legs), binding.upperLegsTextView, binding.upperLegsImageView)
        updateColors(myBundle.bodyPart == getString(R.string.waist), binding.waistTextView, binding.waistImageView)

        return binding.root
    }

    private fun updateColors(stateIsOn: Boolean, textView: TextView, imageView: ImageView) {
        if (stateIsOn) {
            imageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.colorAccent))
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}