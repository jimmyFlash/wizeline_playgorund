package com.wizeline.bookchallenge

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class CustomRadioButton : AppCompatRadioButton {
    private var buttonPadding = 0

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        buttonPadding = resources
            .getDimensionPixelOffset(R.dimen.list_item_button_right_margin)
    }

    override fun getCompoundPaddingLeft(): Int {
        return buttonPadding + super.getCompoundPaddingLeft()
    }
}