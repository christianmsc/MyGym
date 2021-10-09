package com.github.christianmsc.com.github.christianmsc.ui.fragments.variations

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.christianmsc.com.github.christianmsc.adapters.VariationsAdapter
import com.github.christianmsc.com.github.christianmsc.models.ExerciseItem
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.EXERCISE_RESULT_KEY
import com.github.christianmsc.com.github.christianmsc.util.observeOnce
import com.github.christianmsc.com.github.christianmsc.viewmodels.MainViewModel
import com.github.christianmsc.databinding.FragmentVariationsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VariationsFragment : Fragment() {

    private val mAdapter: VariationsAdapter by lazy { VariationsAdapter() }
    private lateinit var mainViewModel: MainViewModel

    private var _binding : FragmentVariationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
       _binding = FragmentVariationsBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: ExerciseItem? = args?.getParcelable(EXERCISE_RESULT_KEY)

        setupRecyclerView()

        lifecycleScope.launch {
            findVariations(myBundle!!.bodyPart)
        }

        return binding.root
    }

    private fun setupRecyclerView(){
        binding.variationsRecyclerView.adapter = mAdapter
        binding.variationsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun findVariations(bodyPart: String) {
        lifecycleScope.launch {
            mainViewModel.readExercises.observeOnce(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    Log.d("ExercisesFragment", "readDatabase called")
                    val variationsList =  database[0].exercise.filter {
                        it.bodyPart == bodyPart
                    }
                    variationsList.let { mAdapter.setData(variationsList) }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}