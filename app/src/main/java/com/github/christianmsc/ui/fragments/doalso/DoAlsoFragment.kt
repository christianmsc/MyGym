package com.github.christianmsc.com.github.christianmsc.ui.fragments.doalso

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.christianmsc.com.github.christianmsc.adapters.DoAlsoAdapter
import com.github.christianmsc.com.github.christianmsc.models.ExerciseItem
import com.github.christianmsc.com.github.christianmsc.util.Constants
import com.github.christianmsc.com.github.christianmsc.util.observeOnce
import com.github.christianmsc.com.github.christianmsc.viewmodels.MainViewModel
import com.github.christianmsc.databinding.FragmentDoAlsoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DoAlsoFragment : Fragment() {

    private var _binding: FragmentDoAlsoBinding? = null
    private val binding get() = _binding!!

    private val mAdapter: DoAlsoAdapter by lazy { DoAlsoAdapter() }
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDoAlsoBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: ExerciseItem? = args?.getParcelable(Constants.EXERCISE_RESULT_KEY)

        setupRecyclerView()

        lifecycleScope.launch {
            findDoAlsoList(myBundle!!.equipment)
        }

        return binding.root
    }

    private fun setupRecyclerView(){
        binding.doAlsoRecyclerView.adapter = mAdapter
        binding.doAlsoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun findDoAlsoList(equipment: String) {
        lifecycleScope.launch {
            mainViewModel.readExercises.observeOnce(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    Log.d("ExercisesFragment", "readDatabase called")
                    val doAlsoList =  database[0].exercise.filter {
                        it.equipment == equipment
                    }
                    doAlsoList.let { mAdapter.setData(doAlsoList) }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}