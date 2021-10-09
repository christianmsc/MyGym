package com.github.christianmsc.ui.fragments.exercises.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.DEFAULT_BODY_PART
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.DEFAULT_TARGET
import com.github.christianmsc.com.github.christianmsc.viewmodels.ExercisesViewModel
import com.github.christianmsc.databinding.ExercisesBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.lang.Exception
import java.util.*

class ExercisesBottomSheet : BottomSheetDialogFragment() {

    private lateinit var exercisesViewModel: ExercisesViewModel

    private var _binding: ExercisesBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var bodyPartChip = DEFAULT_BODY_PART
    private var bodyPartChipId = 0
    private var targetChip = DEFAULT_TARGET
    private var targetChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exercisesViewModel =
            ViewModelProvider(requireActivity()).get(ExercisesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = ExercisesBottomSheetBinding.inflate(inflater, container, false)

        exercisesViewModel.readBodyPartAndTarget.asLiveData().observe(viewLifecycleOwner, { value ->
            bodyPartChip = value.selectedBodyPart
            targetChip = value.selectedTarget
            updateChip(value.selectedBodyPartId, binding.exerciseBodyPartChipGroup)
            updateChip(value.selectedTargetId, binding.exerciseTargetChipGroup)
        })

        binding.exerciseBodyPartChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedBodyPart = chip.text.toString().lowercase(Locale.ROOT)
            bodyPartChip = selectedBodyPart
            bodyPartChipId = selectedChipId
        }

        binding.exerciseTargetChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedTarget = chip.text.toString().lowercase(Locale.ROOT)
            targetChip = selectedTarget
            targetChipId = selectedChipId
        }

        binding.applyBtn.setOnClickListener {
            exercisesViewModel.saveBodyPartAndTargetTemp(
                bodyPartChip,
                bodyPartChipId,
                targetChip,
                targetChipId
            )
            val action =
                ExercisesBottomSheetDirections.actionExercisesBottomSheetToExercisesFragment(true)
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                val targetView = chipGroup.findViewById<Chip>(chipId)
                targetView.isChecked = true
                chipGroup.requestChildFocus(targetView, targetView)
            } catch (e: Exception) {
                Log.d("ExercisesBottomSheet", e.message.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}