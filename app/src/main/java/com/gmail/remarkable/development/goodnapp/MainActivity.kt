package com.gmail.remarkable.development.goodnapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.gmail.remarkable.development.goodnapp.databinding.ActivityMainBinding
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf

private const val day_key = "DAY_KEY"

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var viewModel: DayViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        // Force Dark Mode - just for testing.
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment)
        viewModel = get { parametersOf(navHostFragment) }

        val prevDay = savedInstanceState?.getLong(day_key)
        if (prevDay !== null) viewModel.onNavigateToDay(prevDay)

        setSupportActionBar(binding.toolbar)

        val navController = this.findNavController(R.id.navHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.navHostFragment)
        return navController.navigateUp()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val day = viewModel.mLiveSleepDay.value?.date
        if (day !== null) outState.putLong(day_key, day)
        super.onSaveInstanceState(outState)
    }
}
