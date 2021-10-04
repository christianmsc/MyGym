package com.github.christianmsc.com.github.christianmsc.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.christianmsc.com.github.christianmsc.data.database.entities.FavoritesEntity
import com.github.christianmsc.com.github.christianmsc.util.ExercisesDiffUtil
import com.github.christianmsc.databinding.FavoriteExercisesRowLayoutBinding

class FavoriteExercisesAdapter : RecyclerView.Adapter<FavoriteExercisesAdapter.MyViewHolder>() {

    private var favoriteExercises = emptyList<FavoritesEntity>()

    class MyViewHolder(private val binding: FavoriteExercisesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(favoritesEntity: FavoritesEntity){
                binding.favoritesEntity = favoritesEntity
                binding.executePendingBindings()
            }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteExercisesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteExercisesAdapter.MyViewHolder {
       return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FavoriteExercisesAdapter.MyViewHolder, position: Int) {
       val selectedExercise = favoriteExercises[position]
        holder.bind(selectedExercise)
    }

    override fun getItemCount(): Int {
       return favoriteExercises.size
    }

    fun setData(newFavoriteExercises: List<FavoritesEntity>){
        val favoriteExerciseDiffUtil = ExercisesDiffUtil(favoriteExercises, newFavoriteExercises)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteExerciseDiffUtil)
        favoriteExercises = newFavoriteExercises
        diffUtilResult.dispatchUpdatesTo(this)
    }

}