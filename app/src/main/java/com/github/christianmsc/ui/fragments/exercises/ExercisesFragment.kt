package com.github.christianmsc.com.github.christianmsc.ui.fragments.exercises

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.christianmsc.R
import com.github.christianmsc.com.github.christianmsc.viewmodels.MainViewModel
import com.github.christianmsc.com.github.christianmsc.adapters.ExercisesAdapter
import com.github.christianmsc.com.github.christianmsc.util.NetworkListener
import com.github.christianmsc.com.github.christianmsc.util.NetworkResult
import com.github.christianmsc.com.github.christianmsc.util.observeOnce
import com.github.christianmsc.com.github.christianmsc.viewmodels.ExercisesViewModel
import com.github.christianmsc.databinding.FragmentExercisesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExercisesFragment : Fragment(), SearchView.OnQueryTextListener {

    private val args by navArgs<ExercisesFragmentArgs>()

    private var _binding: FragmentExercisesBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var exercisesViewModel: ExercisesViewModel
    private val mAdapter by lazy { ExercisesAdapter() }
    private lateinit var networkListener: NetworkListener

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

        setHasOptionsMenu(true)

        setupRecyclerView()

        exercisesViewModel.readBackOnline.observe(viewLifecycleOwner, {
            exercisesViewModel.backOnline = it
        })

        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.d("NetworkListener", status.toString())
                    exercisesViewModel.networkStatus = status
                    exercisesViewModel.showNetworkStatus()
                    readDatabase()
                }
        }

        binding.exercisesFab.setOnClickListener {
            if(exercisesViewModel.networkStatus){
                findNavController().navigate(R.id.action_exercisesFragment_to_exercisesBottomSheet)
            }
            else {
                exercisesViewModel.showNetworkStatus()
            }
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerview.adapter = mAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.exercises_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null){
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
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

    private fun searchApiData(searchQuery: String){
        showShimmerEffect()
        mainViewModel.searchExercises(exercisesViewModel.applySearchQuery(searchQuery))
        mainViewModel.searchedExercisesResponse.observe(viewLifecycleOwner, { response ->
            when(response){
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val exercise = response.data
                    exercise?.let {mAdapter.setData(it)}
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
        binding.recyclerview.visibility = View.INVISIBLE
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