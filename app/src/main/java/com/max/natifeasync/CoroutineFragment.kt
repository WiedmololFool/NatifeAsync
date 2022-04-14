package com.max.natifeasync

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.max.natifeasync.databinding.FragmentCoroutineBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlin.random.Random

class CoroutineFragment : Fragment() {

    private var binding: FragmentCoroutineBinding? = null
    private val adapter by lazy {
        NumberListAdapter()
    }
    private val sharedFlow = MutableSharedFlow<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCoroutineBinding.inflate(inflater, container, false)
        this.binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            rcView.adapter = adapter
            rcView.layoutManager = LinearLayoutManager(requireContext())
        }

        val numberList = mutableListOf<Int>()

        lifecycleScope.launchWhenStarted {
                startCoroutine()
        }

        lifecycleScope.launchWhenStarted {
            sharedFlow.asSharedFlow().onEach { number ->
                numberList.add(number)
                adapter.submitList(numberList.toList())
                Log.e(Constants.TAG, numberList.toString())
            }.collect()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private suspend fun startCoroutine() {
        while (true) {
            delay(1000)
            sharedFlow.emit(Random.nextInt(0, 100))
        }
    }

    companion object {

        fun newInstance() = CoroutineFragment()
    }
}