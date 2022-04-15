package com.max.natifeasync

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.max.natifeasync.databinding.FragmentButtonBinding


class ButtonFragment : Fragment() {

    private var binding: FragmentButtonBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentButtonBinding.inflate(inflater, container, false)
        this.binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            btnLiveData.setOnClickListener {
                changeFragment(LiveDataFragment.newInstance())
            }
            btnRxJava.setOnClickListener {
                changeFragment(RxJavaFragment.newInstance())
            }
            btnCoroutines.setOnClickListener {
                changeFragment(CoroutineFragment.newInstance())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun changeFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {

        fun newInstance() = ButtonFragment()

    }
}