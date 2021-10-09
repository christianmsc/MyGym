package com.github.christianmsc.com.github.christianmsc.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.github.christianmsc.R
import com.github.christianmsc.com.github.christianmsc.models.ExerciseItem
import com.github.christianmsc.com.github.christianmsc.util.ExercisesDiffUtil
import com.github.christianmsc.databinding.VariationsRowLayoutBinding
import java.util.*

class VariationsAdapter : RecyclerView.Adapter<VariationsAdapter.MyViewHolder>() {

    private var variationsList = emptyList<ExerciseItem>()

    class MyViewHolder(val binding: VariationsRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(VariationsRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.variationImageView.load(variationsList[position].gifUrl){
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }
        holder.binding.variationName.text = variationsList[position].name.capitalize(Locale.ROOT)
        holder.binding.variationEquipment.text = variationsList[position].equipment
        holder.binding.variationTarget.text = variationsList[position].target
    }

    override fun getItemCount(): Int {
        return variationsList.size
    }

    fun setData(newVariations: List<ExerciseItem>){
        val variationsDiffUtil = ExercisesDiffUtil(variationsList, newVariations)
        val diffUtilResult = DiffUtil.calculateDiff(variationsDiffUtil)
        variationsList = newVariations
        diffUtilResult.dispatchUpdatesTo(this)
    }
}