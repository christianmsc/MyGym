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
import kotlinx.android.synthetic.main.do_also_row_layout.view.*
import java.util.*

class DoAlsoAdapter: RecyclerView.Adapter<DoAlsoAdapter.MyViewHolder>() {

    private var doAlsoList = emptyList<ExerciseItem>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.do_also_row_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.doAlso_imageView.load(doAlsoList[position].gifUrl){
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }
        holder.itemView.doAlso_name.text = doAlsoList[position].name.capitalize(Locale.ROOT)
        holder.itemView.doAlso_bodyPart.text = doAlsoList[position].bodyPart
        holder.itemView.doAlso_target.text = doAlsoList[position].target
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