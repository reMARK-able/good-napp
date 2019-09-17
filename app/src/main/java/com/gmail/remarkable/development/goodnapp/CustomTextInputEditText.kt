package com.gmail.remarkable.development.goodnapp

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText

class CustomTextInputEditText : TextInputEditText, View.OnClickListener {

    init {
        setOnClickListener(this)
    }

    override fun onClick(v: View) {

        v.findNavController().navigate(R.id.action_dayFragment_to_timePickerDialogFragment)
    }

    // Define all 3 constructors since @JvmOverloads doesn't work correctly with defStyleAttr
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onCheckIsTextEditor(): Boolean {
        return false
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        val textTemp = this.text.toString()
        if (focused) {
            this.hint = "SET"
        } else {
            this.hint = ""
        }
    }

}