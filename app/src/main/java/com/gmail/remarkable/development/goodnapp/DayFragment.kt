package com.gmail.remarkable.development.goodnapp


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.gmail.remarkable.development.goodnapp.databinding.FragmentDayBinding

/**
 * A simple [Fragment] subclass.
 */
class DayFragment : Fragment() {

    lateinit var binding: FragmentDayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_day, container, false)

        // For clicking test
        binding.targetTWTEditText.setOnClickListener {
            pickTime(it)
        }

        return binding.root
    }

    fun pickTime(view: View) {
        view.findNavController().navigate(R.id.action_dayFragment_to_timePickerDialogFragment)
    }
}



