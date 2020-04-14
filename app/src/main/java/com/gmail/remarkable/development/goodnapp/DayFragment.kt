package com.gmail.remarkable.development.goodnapp


import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.gmail.remarkable.development.goodnapp.databinding.FragmentDayBinding
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

// DayStart card const identifiers.
const val TARGET_TWT = "targetTwt"
const val WAKE_UP = "wakeUp"
const val OUT_OF_BED = "outOfBed"

// Naps const identifiers.
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

// Night awakes const identifiers.
const val AWAKE_1_START = "awake1start"
const val AWAKE_1_END = "awake1end"
const val AWAKE_2_START = "awake2start"
const val AWAKE_2_END = "awake2end"
const val AWAKE_3_START = "awake3start"
const val AWAKE_3_END = "awake3end"
const val AWAKE_4_START = "awake4start"
const val AWAKE_4_END = "awake4end"
const val AWAKE_5_START = "awake5start"
const val AWAKE_5_END = "awake5end"

// Summary card const identifiers.
const val REAL_BEDTIME = "realBedtime"

/**
 * A Fragment class to get the user input.
 */
class DayFragment : Fragment() {

    val viewModel: DayViewModel by inject { parametersOf(this) }
    lateinit var binding: FragmentDayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_day, container, false)

        binding.viewModel = viewModel
        binding.day = this
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.navigateToConfirmation.observe(viewLifecycleOwner, Observer { action ->
            action?.let { navigateToConfirmation(action) }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.day_fragment_overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_clear -> {
                viewModel.confirmClear()
                return true
            }
            R.id.action_delete_day -> {
                viewModel.confirmDeleteDay()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun pickTime(view: View) {

        val navController = view.findNavController()
        if (navController.currentDestination?.id == R.id.dayFragment) {
            val nameTag = getViewName(view)
            navController.navigate(
                DayFragmentDirections.actionDayFragmentToTimePickerDialogFragment(nameTag)
            )
        }
    }

    fun showAddCommentDialog() {
        CommentDialogFragment().show(parentFragmentManager, "comment_dialog")
    }

    private fun navigateToConfirmation(action: ConfirmActions) {
        val navController = findNavController()
        if (navController.currentDestination?.id == R.id.dayFragment) {
            findNavController().navigate(
                DayFragmentDirections.actionDayFragmentToConfirmDialogFragment(action)
            )
            viewModel.onCompleteNavigationToConfirmDialog()
        }
    }

    // Checks which view was clicked.
    // It works but don't know if i shouldn't be:
    // when {
    // v === binding.nap1.napStartEditText ->
    // for reference equality check???
    private fun getViewName(v: View): String = when (v) {
        binding.cardStart.targetTWTEditText -> TARGET_TWT
        binding.cardStart.wakeUpEditText -> WAKE_UP
        binding.cardStart.outOfBedEditText -> OUT_OF_BED
        binding.cardSummary.realBedtimeEditText -> REAL_BEDTIME

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

        binding.nightAwake1.awakeStartEditText -> AWAKE_1_START
        binding.nightAwake1.awakeEndEditText -> AWAKE_1_END
        binding.nightAwake2.awakeStartEditText -> AWAKE_2_START
        binding.nightAwake2.awakeEndEditText -> AWAKE_2_END
        binding.nightAwake3.awakeStartEditText -> AWAKE_3_START
        binding.nightAwake3.awakeEndEditText -> AWAKE_3_END
        binding.nightAwake4.awakeStartEditText -> AWAKE_4_START
        binding.nightAwake4.awakeEndEditText -> AWAKE_4_END
        binding.nightAwake5.awakeStartEditText -> AWAKE_5_START
        binding.nightAwake5.awakeEndEditText -> AWAKE_5_END
        else -> "unknownView"
    }
}



