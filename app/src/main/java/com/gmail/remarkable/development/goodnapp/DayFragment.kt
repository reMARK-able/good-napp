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

const val NAP_1_START = "nap1start"
const val NAP_1_END = "nap1end"
const val NAP_2_START = "nap2start"
const val NAP_2_END = "nap2end"
const val NAP_3_START = "nap3start"
const val NAP_3_END = "nap3end"
const val NAP_4_START = "nap4start"
const val NAP_4_END = "nap4end"
const val NAP_5_START = "nap5start"
const val NAP_5_END = "nap5end"


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

        binding.nap1.napStartEditText.setOnClickListener {
            pickTime(it)
        }
        binding.nap1.napEndEditText.setOnClickListener {
            pickTime(it)
        }
        binding.viewModel = viewModel
        binding.day = this
        binding.setLifecycleOwner(this)

        return binding.root
    }

    fun pickTime(view: View) {

        val nameTag = getViewName(view)
        view.findNavController()
            .navigate(DayFragmentDirections.actionDayFragmentToTimePickerDialogFragment(nameTag))
    }

    // Checks which view was clicked.
    // It works but don't know if i shouldn't be:
    // when {
    // v === binding.nap1.napStartEditText ->
    // for reference equality check???
    private fun getViewName(v: View): String = when (v) {
        binding.nap1.napStartEditText -> NAP_1_START
        binding.nap1.napEndEditText -> NAP_1_END
        binding.nap2.napStartEditText -> NAP_2_START
        binding.nap2.napEndEditText -> NAP_2_END
        binding.nap3.napStartEditText -> NAP_3_START
        binding.nap3.napEndEditText -> NAP_3_END
        binding.nap4.napStartEditText -> NAP_4_START
        binding.nap4.napEndEditText -> NAP_4_END
        binding.nap5.napStartEditText -> NAP_5_START
        binding.nap5.napEndEditText -> NAP_5_END
        else -> "unknownView"
    }
}



