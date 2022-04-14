package com.max.natifeasync

import android.os.Bundle
import android.os.Handler
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

    private val handler = Handler()
    private var binding: FragmentLiveDataBinding? = null
    private val adapter by lazy {
        NumberListAdapter()
    }

    @Volatile
    var threadIsStopped = false

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
        val liveData = MutableLiveData<Int>()
        startThread(liveData)
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
        stopThread()
    }

    private fun startThread(liveData: MutableLiveData<Int>) {
        threadIsStopped = false

        val runnable = Runnable {
            if (threadIsStopped) return@Runnable
            while (true) {
                Thread.sleep(1000)
                handler.post {
                    liveData.value = Random.nextInt(0, 100)
                }
            }
        }
        Thread(runnable).start()
    }

    private fun stopThread() {
        threadIsStopped = true
    }


    companion object {

        fun newInstance() = LiveDataFragment()

    }
}