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
import com.github.christianmsc.databinding.DoAlsoRowLayoutBinding
import java.util.*

class DoAlsoAdapter: RecyclerView.Adapter<DoAlsoAdapter.MyViewHolder>() {

    private var doAlsoList = emptyList<ExerciseItem>()

    class MyViewHolder(val binding: DoAlsoRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(DoAlsoRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.doAlsoImageView.load(doAlsoList[position].gifUrl){
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }
        holder.binding.doAlsoName.text = doAlsoList[position].name.capitalize(Locale.ROOT)
        holder.binding.doAlsoBodyPart.text = doAlsoList[position].bodyPart
        holder.binding.doAlsoTarget.text = doAlsoList[position].target
    }

    override fun getItemCount(): Int {
        return doAlsoList.size
    }

    fun setData(newDoAlsoList: List<ExerciseItem>){
        val doAlsoDiffUtil = ExercisesDiffUtil(doAlsoList, newDoAlsoList)
        val diffUtilResult = DiffUtil.calculateDiff(doAlsoDiffUtil)
        doAlsoList = newDoAlsoList
        diffUtilResult.dispatchUpdatesTo(this)
    }
}