package com.gmail.remarkable.development.goodnapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gmail.remarkable.development.goodnapp.databinding.FragmentAboutBinding
import com.mikepenz.aboutlibraries.LibsBuilder

/**
 * Fragment to display something about the app.
 */
class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAboutBinding.inflate(inflater, container, false)

        val versionName = BuildConfig.VERSION_NAME
        binding.aboutVersionTextView.append(versionName)

        binding.aboutLicensesTextView.setOnClickListener { goToLibrariesActivity() }

        return binding.root
    }

    private fun goToLibrariesActivity() {
        LibsBuilder()
            .withActivityTitle(getString(R.string.open_source_licenses))
            .withAboutIconShown(false)
            .withLicenseShown(true)
            .start(requireContext())
    }

}
