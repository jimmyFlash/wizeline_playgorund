package com.wizeline.bookchallenge.views

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wizeline.bookchallenge.MyApplication
import com.wizeline.bookchallenge.adapters.HistoryAdapter
import com.wizeline.bookchallenge.databinding.HistoryFragmentBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryFragment : Fragment() {

    private lateinit var readSavedImageJOb: Job
    private lateinit var loadImagesJob: Job
    lateinit var historyFragmentBinding: HistoryFragmentBinding

    private val historyAdapter = HistoryAdapter()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        fun newInstance() = HistoryFragment()
    }

    private lateinit var viewModel: HistoryViewModel

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        historyFragmentBinding = HistoryFragmentBinding.inflate(inflater, container,
            false)

        (requireActivity() as? MainActivity)?.
                setSupportActionBar(historyFragmentBinding.historyToolbar)
        historyFragmentBinding.historyToolbar.setNavigationOnClickListener {
            val action = HistoryFragmentDirections.nextAction()
            findNavController().navigate(action)
        }

        historyFragmentBinding.historyRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        historyFragmentBinding.historyRecyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))

        historyFragmentBinding.historyRecyclerView.adapter = historyAdapter

        return historyFragmentBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as MyApplication).appComponent.inject(this)
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
       loadImagesJob =  lifecycleScope.launchWhenCreated {
            context?.let {
                viewModel.loadImages(it)
            }
            setupObserver()
        }

    }

    @ExperimentalCoroutinesApi
    private fun setupObserver() {
       readSavedImageJOb = lifecycleScope.launch {
            val savedImageList = viewModel.getSavedImages()
            savedImageList.collect {
                historyAdapter.swap(it)
            }
        }
    }

    override fun onDestroy() {
        readSavedImageJOb.cancel()
        loadImagesJob.cancel()
        super.onDestroy()
    }

}