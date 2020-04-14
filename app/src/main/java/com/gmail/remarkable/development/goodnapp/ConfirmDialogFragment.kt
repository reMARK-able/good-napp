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

            val message = setupMessage(action)
            val stringButtonPositive = setupPositiveButtonString(action)
            val title = setupTitle(action)

            val builder = MaterialAlertDialogBuilder(it)
            title?.let { builder.setTitle(it) }
            builder.setMessage(message)
                .setPositiveButton(
                    stringButtonPositive,
                    DialogInterface.OnClickListener { dialog, id ->
                        when (action) {
                            is ConfirmActions.DeleteNap -> deleteNap(action.index)
                            is ConfirmActions.DeleteAwake -> deleteAwake(action.index)
                            ConfirmActions.ClearAll -> clearDay()
                            ConfirmActions.DeleteDay -> deleteDay()
                        }
                    })
                .setNegativeButton(
                    R.string.cancel_button,
                    DialogInterface.OnClickListener { dialog, id ->
                        // Cancel...
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setupTitle(action: ConfirmActions): Int? = when (action) {
        ConfirmActions.ClearAll -> R.string.clear_all_confirm_title
        ConfirmActions.DeleteDay -> R.string.delete_the_day_confirm_title
        else -> null
    }

    private fun setupMessage(action: ConfirmActions): Int = when (action) {
        is ConfirmActions.DeleteNap -> R.string.delete_the_nap
        is ConfirmActions.DeleteAwake -> R.string.delete_the_night_awake
        ConfirmActions.ClearAll -> R.string.clear_all_confirm_message
        ConfirmActions.DeleteDay -> R.string.delete_day_confirm_message
    }


    private fun setupPositiveButtonString(action: ConfirmActions): Int = when (action) {
        ConfirmActions.ClearAll -> R.string.clear
        else -> R.string.delete
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
