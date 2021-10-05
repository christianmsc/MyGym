package com.github.christianmsc.com.github.christianmsc.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.christianmsc.R
import com.github.christianmsc.com.github.christianmsc.data.database.entities.FavoritesEntity
import com.github.christianmsc.com.github.christianmsc.ui.fragments.favorites.FavoritesExercisesFragmentDirections
import com.github.christianmsc.com.github.christianmsc.util.ExercisesDiffUtil
import com.github.christianmsc.com.github.christianmsc.viewmodels.MainViewModel
import com.github.christianmsc.databinding.FavoriteExercisesRowLayoutBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favorite_exercises_row_layout.view.*

class FavoriteExercisesAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
) : RecyclerView.Adapter<FavoriteExercisesAdapter.MyViewHolder>(), ActionMode.Callback {

    private var multiSelection = false

    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View

    private var selectedExercises = arrayListOf<FavoritesEntity>()
    private var myViewHolders = arrayListOf<MyViewHolder>()
    private var favoriteExercises = emptyList<FavoritesEntity>()

    class MyViewHolder(private val binding: FavoriteExercisesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoritesEntity: FavoritesEntity) {
            binding.favoritesEntity = favoritesEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    FavoriteExercisesRowLayoutBinding.inflate(layoutInflater, parent, false)
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

        myViewHolders.add(holder)
        rootView = holder.itemView.rootView

        val currentExercise = favoriteExercises[position]
        holder.bind(currentExercise)

        /**
         * Single click listener
         */
        holder.itemView.favoriteExercisesRowLayout.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, currentExercise)
            } else {
                val action =
                    FavoritesExercisesFragmentDirections.actionFavoritesExercisesFragmentToDetailsActivity(
                        currentExercise.exerciseItem
                    )
                holder.itemView.findNavController().navigate(action)
            }
        }

        /**
         * Long click listener
         */
        holder.itemView.favoriteExercisesRowLayout.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentExercise)
                true
            } else {
                multiSelection = false
                false
            }
        }
    }

    private fun applySelection(holder: MyViewHolder, currentExercise: FavoritesEntity) {
        if (selectedExercises.contains(currentExercise)) {
            selectedExercises.remove(currentExercise)
            changeExerciseStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
            applyActionModeTitle()
        } else {
            selectedExercises.add(currentExercise)
            changeExerciseStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
            applyActionModeTitle()
        }
    }

    private fun changeExerciseStyle(holder: MyViewHolder, backgorundColor: Int, strokeColor: Int) {
        holder.itemView.favoriteExercisesRowLayout.setBackgroundColor(
            ContextCompat.getColor(requireActivity, backgorundColor)
        )
        holder.itemView.favorite_row_cardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun applyActionModeTitle() {
        when (selectedExercises.size) {
            0 -> {
                mActionMode.finish()
            }
            1 -> {
                mActionMode.title = "${selectedExercises.size} item selected"
            }
            else -> {
                mActionMode.title = "${selectedExercises.size} items selected"
            }
        }
    }

    override fun getItemCount(): Int {
        return favoriteExercises.size
    }

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favorites_contextual_menu, menu)
        mActionMode = actionMode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        if (menu?.itemId == R.id.delete_favorite_exercise_menu) {
            selectedExercises.forEach {
                mainViewModel.deleteFavoriteExercise(it)
            }
            showSnackBar("${selectedExercises.size} Exercise/s removed.")
            multiSelection = false
            selectedExercises.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        myViewHolders.forEach { holder ->
            changeExerciseStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }
        multiSelection = false
        selectedExercises.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

    fun setData(newFavoriteExercises: List<FavoritesEntity>) {
        val favoriteExerciseDiffUtil = ExercisesDiffUtil(favoriteExercises, newFavoriteExercises)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteExerciseDiffUtil)
        favoriteExercises = newFavoriteExercises
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

    fun clearContextualActionMode(){
        if(this::mActionMode.isInitialized){
            mActionMode.finish()
        }
    }

}