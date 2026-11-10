package com.github.andreyasadchy.xtra.util

object TvAutoQualityPolicy {
    fun preferHighest(isTv: Boolean, unmetered: Boolean) = isTv || unmetered

    fun maxHeight(automatic: Boolean, recoveryCap: Int? = null): Int {
        if (!automatic) return Int.MAX_VALUE
        return recoveryCap?.takeIf { it > 0 } ?: Int.MAX_VALUE
    }
}
