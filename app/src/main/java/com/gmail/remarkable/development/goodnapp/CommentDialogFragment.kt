package com.gmail.remarkable.development.goodnapp

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

/**
 * Dialog fragment for adding the comment.
 */
class CommentDialogFragment : DialogFragment() {

    private val viewModel: DayViewModel by inject { parametersOf(this) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val customView = it.layoutInflater.inflate(R.layout.fragment_comment_dialog, null)
            val commentEditText =
                customView.findViewById<TextInputEditText>(R.id.dialog_comment_textInputEditText)
            commentEditText.setText(viewModel.mDay.comment)
            val builder = MaterialAlertDialogBuilder(it)
                .setView(customView)
                .setPositiveButton(
                    "Save",
                    DialogInterface.OnClickListener { dialog, id ->
                        val comment = commentEditText.text.toString()
                        saveComment(comment)
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        // Cancel...
                    })
            // Create the AlertDialog object and return it
            builder.create().also { isCancelable = false }
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun saveComment(comment: String) {
        viewModel.saveComment(comment)
    }

}
