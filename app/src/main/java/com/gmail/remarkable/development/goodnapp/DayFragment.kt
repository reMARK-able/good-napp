package com.gmail.remarkable.development.goodnapp


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.gmail.remarkable.development.goodnapp.databinding.FragmentDayBinding

/**
 * A Fragment class to get the user input.
 */
class DayFragment : Fragment() {

    private val viewModel: DayViewModel by navGraphViewModels(R.id.navigation)

    lateinit var binding: FragmentDayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_day, container, false)

        return binding.root
    }

    private fun pickTime(view: View) {
        view.findNavController().navigate(R.id.action_dayFragment_to_timePickerDialogFragment)
    }
}



