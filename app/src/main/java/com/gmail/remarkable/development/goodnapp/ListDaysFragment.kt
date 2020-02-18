package com.gmail.remarkable.development.goodnapp


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gmail.remarkable.development.goodnapp.databinding.FragmentListDaysBinding
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

/**
 * Fragment to display a list of SleepDays.
 */
class ListDaysFragment : Fragment() {

    val viewModel: DayViewModel by inject { parametersOf(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentListDaysBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_list_days, container, false)

        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        val adapter = SleepDayAdapter(SleepDayListener { date ->
            navigateToDay(date)
        })
        binding.dayList.adapter = adapter

        viewModel.daysInPairs.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigateToToday.observe(viewLifecycleOwner, Observer { navigate ->
            navigate?.let {
                if (navigate) {
                    navigateToToday()
                }
            }
        })

        return binding.root
    }

    private fun navigateToToday() {
        viewModel.onNavigateToToday()
        val navController = findNavController()
        if (navController.currentDestination?.id == R.id.listDaysFragment) {
            findNavController().navigate(ListDaysFragmentDirections.actionListDaysFragmentToDayFragment())
        }
    }

    private fun navigateToDay(date: Long) {
        viewModel.onNavigateToDay(date)
        val navController = findNavController()
        if (navController.currentDestination?.id == R.id.listDaysFragment) {
            findNavController().navigate(ListDaysFragmentDirections.actionListDaysFragmentToDayFragment())
        }
    }


}
