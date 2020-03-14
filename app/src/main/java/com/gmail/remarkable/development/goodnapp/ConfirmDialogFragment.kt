package com.gmail.remarkable.development.goodnapp

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.parcel.Parcelize

/**
 * Dialog Fragment for delete/clear action confirmation.
 */
class ConfirmDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = MaterialAlertDialogBuilder(it)
            builder.setMessage("Do you really?")
                .setPositiveButton("ok",
                    DialogInterface.OnClickListener { dialog, id ->
                        // Proceed...
                    })
                .setNegativeButton("cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        // Cancel...
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
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
