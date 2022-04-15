package com.max.natifeasync

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.max.natifeasync.databinding.FragmentLiveDataBinding
import kotlin.random.Random


class LiveDataFragment : Fragment() {

    private var binding: FragmentLiveDataBinding? = null
    private val adapter by lazy {
        NumberListAdapter()
    }
    private val liveData = MutableLiveData<Int>()

    private val thread: Thread by lazy {
        Thread {
            while (!thread.isInterrupted) {
                try {
                    Thread.sleep(1000)
                    liveData.postValue(Random.nextInt(0, 100))
                } catch (e: InterruptedException) {
                    Log.e(Constants.TAG, e.message.toString())
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLiveDataBinding.inflate(inflater, container, false)
        this.binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startThread()
        binding?.apply {
            rcView.adapter = adapter
            rcView.layoutManager = LinearLayoutManager(requireContext())
        }
        val numberList = mutableListOf<Int>()
        liveData.observe(viewLifecycleOwner) { number ->
            numberList.add(number)
            adapter.submitList(numberList.toList())
            Log.e(Constants.TAG, numberList.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopThread()
    }

    private fun startThread() {
        thread.start()
    }

    private fun stopThread() {
        thread.interrupt()
    }

    companion object {

        fun newInstance() = LiveDataFragment()

    }
}