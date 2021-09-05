package com.github.christianmsc.com.github.christianmsc.ui.fragments.exercises

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.christianmsc.R
import com.github.christianmsc.com.github.christianmsc.viewmodels.MainViewModel
import com.github.christianmsc.com.github.christianmsc.adapters.ExercisesAdapter
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.API_HOST
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.API_KEY
import com.github.christianmsc.com.github.christianmsc.util.NetworkResult
import com.github.christianmsc.com.github.christianmsc.viewmodels.ExercisesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_exercises.view.*

@AndroidEntryPoint
class ExercisesFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var exercisesViewModel: ExercisesViewModel
    private val mAdapter by lazy { ExercisesAdapter() }
    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        exercisesViewModel = ViewModelProvider(requireActivity()).get(ExercisesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_exercises, container, false)

        setupRecyclerView()
        requestApiData()

        return mView
    }

    private fun requestApiData(){
        mainViewModel.getExercises(exercisesViewModel.applyHeaders())
        mainViewModel.exercisesResponse.observe(viewLifecycleOwner, { response ->
            when(response){
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    mView.recyclerview.visibility = View.VISIBLE
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
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

    private fun setupRecyclerView() {
        mView.recyclerview.adapter = mAdapter
        mView.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun showShimmerEffect() {
        mView.shimmer_view_container.visibility = View.VISIBLE
        mView.shimmer_view_container.showShimmer(true)
    }

    private fun hideShimmerEffect() {
        mView.shimmer_view_container.hideShimmer()
        mView.shimmer_view_container.visibility = View.GONE
    }

}