package com.gmail.remarkable.development.goodnapp

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.parcel.Parcelize
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

/**
 * Dialog Fragment for delete/clear action confirmation.
 */
class ConfirmDialogFragment : DialogFragment() {

    private val viewModel: DayViewModel by inject { parametersOf(this) }

    private val args: ConfirmDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val action = args.confirmAction

            val builder = MaterialAlertDialogBuilder(it)
            builder.setMessage("Do you really?")
                .setPositiveButton("ok",
                    DialogInterface.OnClickListener { dialog, id ->
                        when (action) {
                            is ConfirmActions.DeleteNap -> deleteNap(action.index)
                            is ConfirmActions.DeleteAwake -> deleteAwake(action.index)
                            ConfirmActions.ClearAll -> clearDay()
                            ConfirmActions.DeleteDay -> deleteDay()
                        }
                    })
                .setNegativeButton("cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        // Cancel...
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun deleteNap(index: Int) {
        viewModel.deleteNap(index)
    }

    private fun deleteAwake(index: Int) {
        viewModel.deleteAwake(index)
    }

    private fun clearDay() {
        viewModel.clearDay()
    }


    private fun deleteDay() {
        viewModel.deleteDay()
        navigateBackToListDaysFragment()
    }

    private fun navigateBackToListDaysFragment() {
        val navController = findNavController()
        if (navController.currentDestination?.id == R.id.confirmDialogFragment) {
            navController.navigate(
                ConfirmDialogFragmentDirections.actionConfirmDialogFragmentToListDaysFragment()
            )
        }
    }

}

sealed class ConfirmActions : Parcelable {

    @Parcelize
    object DeleteDay : ConfirmActions()

    @Parcelize
    object ClearAll : ConfirmActions()

    @Parcelize
    class DeleteNap(val index: Int) : ConfirmActions()

    @Parcelize
    class DeleteAwake(val index: Int) : ConfirmActions()

}
