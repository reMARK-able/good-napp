package com.gmail.remarkable.development.goodnapp

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText

/***
 * Custom TextInputEditText to handle the focus and data input.
 */
class CustomTextInputEditText : TextInputEditText {

    // Define all 3 constructors since @JvmOverloads doesn't work correctly with defStyleAttr.
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    // Overriding this method with return false prevent auto-open soft input keyboard.
    override fun onCheckIsTextEditor(): Boolean {
        return false
    }

    // On non-touchable devices sets hint on outlined text fields.
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