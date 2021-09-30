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
import com.github.christianmsc.R
import com.github.christianmsc.com.github.christianmsc.adapters.DoAlsoAdapter
import com.github.christianmsc.com.github.christianmsc.models.ExerciseItem
import com.github.christianmsc.com.github.christianmsc.util.Constants
import com.github.christianmsc.com.github.christianmsc.util.observeOnce
import com.github.christianmsc.com.github.christianmsc.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_do_also.view.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DoAlsoFragment : Fragment() {

    private val mAdapter: DoAlsoAdapter by lazy { DoAlsoAdapter() }
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
        val view = inflater.inflate(R.layout.fragment_do_also, container, false)

        val args = arguments
        val myBundle: ExerciseItem? = args?.getParcelable(Constants.EXERCISE_RESULT_KEY)

        setupRecyclerView(view)

        lifecycleScope.launch {
            findDoAlsoList(myBundle!!.equipment)
        }

        return view
    }

    private fun setupRecyclerView(view: View){
        view.doAlso_recyclerView.adapter = mAdapter
        view.doAlso_recyclerView.layoutManager = LinearLayoutManager(requireContext())
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

}