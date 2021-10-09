package com.github.christianmsc.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.github.christianmsc.R
import com.github.christianmsc.com.github.christianmsc.adapters.PagerAdapter
import com.github.christianmsc.com.github.christianmsc.data.database.entities.FavoritesEntity
import com.github.christianmsc.com.github.christianmsc.ui.fragments.doalso.DoAlsoFragment
import com.github.christianmsc.com.github.christianmsc.ui.fragments.overview.OverviewFragment
import com.github.christianmsc.com.github.christianmsc.ui.fragments.variations.VariationsFragment
import com.github.christianmsc.com.github.christianmsc.viewmodels.MainViewModel
import com.github.christianmsc.databinding.ActivityDetailsBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()

    private var exerciseSaved = false
    private var savedExerciseId = 0

    private lateinit var menuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(VariationsFragment())
        fragments.add(DoAlsoFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Variations")
        titles.add("Do Also")

        val resultBundle = Bundle()
        resultBundle.putParcelable("exerciseBundle", args.result)

        val pagerAdapter = PagerAdapter(
            resultBundle,
            fragments,
            this
        )

        binding.viewPager2.isUserInputEnabled = false
        binding.viewPager2.apply {
            adapter = pagerAdapter
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = titles[position]
        }.attach()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        menuItem = menu!!.findItem(R.id.save_to_favorites_menu)
        checkSavedExercises(menuItem)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_to_favorites_menu && !exerciseSaved) {
            saveToFavorites(item)
        } else if (item.itemId == R.id.save_to_favorites_menu && exerciseSaved) {
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkSavedExercises(menuItem: MenuItem) {
        mainViewModel.readFavoriteExercises.observe(this, { favoritesEntity ->
            try {
                for (savedExercise in favoritesEntity) {
                    if (savedExercise.exerciseItem.id == args.result.id) {
                        changeMenuItemColor(menuItem, R.color.yellow)
                        savedExerciseId = savedExercise.id
                        exerciseSaved = true
                    }
                }
            } catch (e: Exception) {
                Log.d("DetailsActivity", e.message.toString())
            }
        })
    }

    private fun saveToFavorites(item: MenuItem) {
        val favoritesEntity = FavoritesEntity(
            0,
            args.result
        )
        mainViewModel.insertFavoriteExercise(favoritesEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Exercise saved.")
        exerciseSaved = true
    }

    private fun removeFromFavorites(item: MenuItem) {
        val favoritesEntity = FavoritesEntity(
            savedExerciseId,
            args.result
        )
        mainViewModel.deleteFavoriteExercise(favoritesEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Removed from Favorites.")
        exerciseSaved = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.detailsLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }

    override fun onDestroy() {
        super.onDestroy()
        changeMenuItemColor(menuItem, R.color.white)
    }
}