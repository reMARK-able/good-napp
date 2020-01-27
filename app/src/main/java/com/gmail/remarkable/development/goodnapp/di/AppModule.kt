package com.gmail.remarkable.development.goodnapp.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.gmail.remarkable.development.goodnapp.DayViewModel
import com.gmail.remarkable.development.goodnapp.DayViewModelFactory
import com.gmail.remarkable.development.goodnapp.R
import com.gmail.remarkable.development.goodnapp.database.SleepDatabase
import org.koin.dsl.module

val appModule = module {

    // SleepDatabase.
    single { SleepDatabase.create(get()) }

    // SleepDatabaseDao.
    single { get<SleepDatabase>().sleepDatabaseDao }

    // DayViewModelFactory.
    factory { DayViewModelFactory(get(), get()) }

    // DayViewModel scoped to navigation graph.
    factory { (fragment: Fragment) ->
        val navController = NavHostFragment.findNavController(fragment)
        val owner = navController.getViewModelStoreOwner(R.id.navigation)
        val provider = ViewModelProvider(owner, get<DayViewModelFactory>())
        provider.get(DayViewModel::class.java)
    }

}


