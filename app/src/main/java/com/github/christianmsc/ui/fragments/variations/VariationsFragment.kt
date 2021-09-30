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
import com.github.christianmsc.R
import com.github.christianmsc.com.github.christianmsc.adapters.VariationsAdapter
import com.github.christianmsc.com.github.christianmsc.models.ExerciseItem
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.EXERCISE_RESULT_KEY
import com.github.christianmsc.com.github.christianmsc.util.observeOnce
import com.github.christianmsc.com.github.christianmsc.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_variations.view.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VariationsFragment : Fragment() {

    private val mAdapter: VariationsAdapter by lazy { VariationsAdapter() }
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_variations, container, false)

        val args = arguments
        val myBundle: ExerciseItem? = args?.getParcelable(EXERCISE_RESULT_KEY)

        setupRecyclerView(view)

        lifecycleScope.launch {
            findVariations(myBundle!!.bodyPart)
        }

        return view
    }

    private fun setupRecyclerView(view: View){
        view.variations_recyclerView.adapter = mAdapter
        view.variations_recyclerView.layoutManager = LinearLayoutManager(requireContext())
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

}