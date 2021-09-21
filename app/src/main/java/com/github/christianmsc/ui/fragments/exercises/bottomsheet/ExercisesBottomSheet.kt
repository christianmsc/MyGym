package com.github.christianmsc.ui.fragments.exercises.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.github.christianmsc.R
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.DEFAULT_BODY_PART
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.DEFAULT_TARGET
import com.github.christianmsc.com.github.christianmsc.viewmodels.ExercisesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.exercises_bottom_sheet.view.*
import java.lang.Exception
import java.util.*

class ExercisesBottomSheet : BottomSheetDialogFragment() {

    private lateinit var exercisesViewModel: ExercisesViewModel

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
    ): View? {
        // Inflate the layout for this fragment
        val mView = inflater.inflate(R.layout.exercises_bottom_sheet, container, false)

        exercisesViewModel.readBodyPartAndTarget.asLiveData().observe(viewLifecycleOwner, { value ->
            bodyPartChip = value.selectedBodyPart
            targetChip = value.selectedTarget
            updateChip(value.selectedBodyPartId, mView.exerciseBodyPart_chipGroup)
            updateChip(value.selectedTargetId, mView.exerciseTarget_chipGroup)
        })

        mView.exerciseBodyPart_chipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedBodyPart = chip.text.toString().lowercase(Locale.ROOT)
            bodyPartChip = selectedBodyPart
            bodyPartChipId = selectedChipId
        }

        mView.exerciseTarget_chipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedTarget = chip.text.toString().lowercase(Locale.ROOT)
            targetChip = selectedTarget
            targetChipId = selectedChipId
        }

        mView.apply_btn.setOnClickListener {
            exercisesViewModel.saveBodyPartAndTarget(
                bodyPartChip,
                bodyPartChipId,
                targetChip,
                targetChipId
            )
            val action = ExercisesBottomSheetDirections.actionExercisesBottomSheetToExercisesFragment(true)
            findNavController().navigate(action)
        }

        return mView
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.d("ExercisesBottomSheet", e.message.toString())
            }
        }
    }

}