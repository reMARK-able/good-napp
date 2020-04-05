package com.gmail.remarkable.development.goodnapp

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

/**
 * Dialog fragment for adding the comment.
 */
class CommentDialogFragment : DialogFragment() {

    private val viewModel: DayViewModel by inject { parametersOf(this) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = MaterialAlertDialogBuilder(it)
                .setView(layoutInflater.inflate(R.layout.fragment_comment_dialog, null))
                .setPositiveButton(
                    "Save",
                    DialogInterface.OnClickListener { dialog, id ->

                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        // Cancel...
                    })
            // Create the AlertDialog object and return it
            builder.create().also { isCancelable = false }
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}
