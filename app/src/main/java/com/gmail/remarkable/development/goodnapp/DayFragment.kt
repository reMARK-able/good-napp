package com.gmail.remarkable.development.goodnapp


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.gmail.remarkable.development.goodnapp.databinding.FragmentDayBinding

/**
 * A Fragment class to get the user input.
 */
class DayFragment : Fragment() {

    lateinit var binding: FragmentDayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_day, container, false)

        return binding.root
    }

}



