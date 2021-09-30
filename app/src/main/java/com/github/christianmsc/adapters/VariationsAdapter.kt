package com.github.christianmsc.com.github.christianmsc.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.github.christianmsc.R
import com.github.christianmsc.com.github.christianmsc.models.ExerciseItem
import com.github.christianmsc.com.github.christianmsc.util.ExercisesDiffUtil
import kotlinx.android.synthetic.main.variations_row_layout.view.*
import java.util.*

class VariationsAdapter : RecyclerView.Adapter<VariationsAdapter.MyViewHolder>() {

    private var variationsList = emptyList<ExerciseItem>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.variations_row_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.variation_imageView.load(variationsList[position].gifUrl){
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }
        holder.itemView.variation_name.text = variationsList[position].name.capitalize(Locale.ROOT)
        holder.itemView.variation_equipment.text = variationsList[position].equipment
        holder.itemView.variation_target.text = variationsList[position].target
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