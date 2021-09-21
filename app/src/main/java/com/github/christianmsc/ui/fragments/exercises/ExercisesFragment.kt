package com.github.christianmsc.com.github.christianmsc.ui.fragments.exercises

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.christianmsc.R
import com.github.christianmsc.com.github.christianmsc.viewmodels.MainViewModel
import com.github.christianmsc.com.github.christianmsc.adapters.ExercisesAdapter
import com.github.christianmsc.com.github.christianmsc.util.NetworkResult
import com.github.christianmsc.com.github.christianmsc.util.observeOnce
import com.github.christianmsc.com.github.christianmsc.viewmodels.ExercisesViewModel
import com.github.christianmsc.databinding.FragmentExercisesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExercisesFragment : Fragment() {

    private val args by navArgs<ExercisesFragmentArgs>()

    private var _binding: FragmentExercisesBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var exercisesViewModel: ExercisesViewModel
    private val mAdapter by lazy { ExercisesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        exercisesViewModel =
            ViewModelProvider(requireActivity()).get(ExercisesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentExercisesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        setupRecyclerView()
        readDatabase()

        binding.exercisesFab.setOnClickListener{
            findNavController().navigate(R.id.action_exercisesFragment_to_exercisesBottomSheet)
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerview.adapter = mAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readExercises.observeOnce(viewLifecycleOwner, { database ->
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    Log.d("ExercisesFragment", "readDatabase called")
                    mAdapter.setData(database[0].exercise)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            })
        }
    }

    private fun requestApiData() {
        Log.d("ExercisesFragment", "requestApiData called")
        mainViewModel.getExercises(exercisesViewModel.applyQueries())
        mainViewModel.exercisesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let { mAdapter.setData(it) }
                    hideShimmerEffect()
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readExercises.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database[0].exercise)
                }
            })
        }
    }

    private fun showShimmerEffect() {
        binding.shimmerViewContainer.visibility = View.VISIBLE
        binding.shimmerViewContainer.showShimmer(true)
    }

    private fun hideShimmerEffect() {
        binding.shimmerViewContainer.hideShimmer()
        binding.shimmerViewContainer.visibility = View.GONE
        binding.recyclerview.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}