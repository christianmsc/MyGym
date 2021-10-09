package com.github.christianmsc.com.github.christianmsc.ui.fragments.randomexercise

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.github.christianmsc.R
import com.github.christianmsc.com.github.christianmsc.util.NetworkResult
import com.github.christianmsc.com.github.christianmsc.viewmodels.MainViewModel
import com.github.christianmsc.databinding.FragmentRandomExerciseBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RandomExerciseFragment : Fragment() {

    private val mainViewModel by viewModels<MainViewModel>()

    private var _binding: FragmentRandomExerciseBinding? = null
    private val binding get() = _binding!!

    private var randomExercise = "No random exercise"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRandomExerciseBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel

        setHasOptionsMenu(true)

        val randomExerciseId = (1..3888).random().toString().padStart(4, '0')
        mainViewModel.getExercise(randomExerciseId)
        mainViewModel.exerciseResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    Glide.with(binding.root).load(response.data?.gifUrl)
                        .into(binding.randomExerciseImageView);
                    binding.randomExerciseTextView.text = response.data?.name
                    if (response.data != null) {
                        randomExercise = response.data?.name
                    }
                }
                is NetworkResult.Error -> {
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    Log.d("RandomExerciseFragment", "Loading")
                }
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.random_exercise_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share_random_exercise_menu) {
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, randomExercise)
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRandomExercise.observe(viewLifecycleOwner, { database ->
                if (!database.isNullOrEmpty()) {
                    Glide.with(binding.root).load(database[0].randomExercise.gifUrl)
                        .into(binding.randomExerciseImageView);
                    binding.randomExerciseTextView.text = database[0].randomExercise.name
                    randomExercise = database[0].randomExercise.name
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}