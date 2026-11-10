package com.github.andreyasadchy.xtra.ui.view

import android.text.TextUtils
import android.widget.TextView

/**
 * TV: the title of the focused card scrolls when it is longer than the card, a second after the focus (not while going
 * through a row), and comes back to its start with "…" when the card loses the focus.
 */
object TvMarquee {

    // Lite TV: keep labels stable instead of starting delayed per-card animations.
    @Suppress("UNUSED_PARAMETER")
    fun update(title: TextView, focused: Boolean) {
        (title.getTag(com.github.andreyasadchy.xtra.R.id.tvMarqueeStart) as? Runnable)?.let {
            title.removeCallbacks(it)
            title.setTag(com.github.andreyasadchy.xtra.R.id.tvMarqueeStart, null)
        }
        if (title.isSelected) title.isSelected = false
        if (title.ellipsize != TextUtils.TruncateAt.END) title.ellipsize = TextUtils.TruncateAt.END
    }
}
