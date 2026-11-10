package com.github.andreyasadchy.xtra.util

object TvAutoQualityPolicy {
    fun maxHeight(isTv: Boolean, automatic: Boolean, recoveryCap: Int? = null): Int {
        if (!automatic) return Int.MAX_VALUE
        return minOf(if (isTv) 1080 else Int.MAX_VALUE, recoveryCap?.takeIf { it > 0 } ?: Int.MAX_VALUE)
    }
}
