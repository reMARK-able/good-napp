package com.gmail.remarkable.development.goodnapp


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.gmail.remarkable.development.goodnapp.databinding.FragmentListDaysBinding
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

/**
 * Fragment to display a list of SleepDays.
 */
class ListDaysFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentListDaysBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_list_days, container, false)

        binding.setLifecycleOwner(this)

        val viewModel: DayViewModel by inject { parametersOf(this) }

        val adapter = SleepDayAdapter()
        binding.dayList.adapter = adapter

        viewModel.days.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }


}
