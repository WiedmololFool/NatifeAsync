package com.max.natifeasync

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.max.natifeasync.databinding.FragmentRxJavaBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.random.Random


class RxJavaFragment : Fragment() {

    private var binding: FragmentRxJavaBinding? = null
    private val disposables = CompositeDisposable()
    private val adapter by lazy {
        NumberListAdapter()
    }

    @Volatile
    var threadIsWorking= true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentRxJavaBinding.inflate(inflater, container, false)
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
        disposables.add(dataSource()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ number ->
                numberList.add(number)
                adapter.submitList(numberList.toList())
                Log.e(Constants.TAG, numberList.toString())
            },{
                Log.e(Constants.TAG,it.message.toString())
            },{
                Log.e(Constants.TAG,"Complete")
            }))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        stopThread()
    }

    private fun dataSource(): Observable<Int> {
        threadIsWorking = true
        return Observable.create { subscriber ->
            while (threadIsWorking) {
                Thread.sleep(1000)
                subscriber.onNext(Random.nextInt(0, 100))
            }
            subscriber.onComplete()
        }
    }

    private fun stopThread(){
        threadIsWorking = false
    }

    companion object {

        fun newInstance() = RxJavaFragment()
    }
}