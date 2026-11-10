package com.github.andreyasadchy.xtra.ui.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class TextWithShadow : AppCompatTextView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        // TV badges already have a contrasting background; draw their glyphs only once.
        val passes = if (resources.getBoolean(com.github.andreyasadchy.xtra.R.bool.isTv)) 1 else 7
        repeat(passes) {
            super.onDraw(canvas)
        }
    }
}