package com.github.christianmsc.com.github.christianmsc.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.christianmsc.com.github.christianmsc.models.Exercise
import com.github.christianmsc.com.github.christianmsc.models.ExerciseItem
import com.github.christianmsc.com.github.christianmsc.util.ExercisesDiffUtil
import com.github.christianmsc.databinding.ExercisesRowLayoutBinding

class ExercisesAdapter : RecyclerView.Adapter<ExercisesAdapter.MyViewHolder>() {

    private var exercises = emptyList<ExerciseItem>()

    class MyViewHolder(private val binding: ExercisesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(result: ExerciseItem){
                binding.result = result
                binding.executePendingBindings()
            }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ExercisesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentExercise = exercises[position]
        holder.bind(currentExercise)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    fun setData(newData: Exercise) {
        val exercisesDiffUtil = ExercisesDiffUtil(exercises, newData)
        val diffUtilExercise = DiffUtil.calculateDiff(exercisesDiffUtil)
        exercises = newData
        diffUtilExercise.dispatchUpdatesTo(this)
    }
}