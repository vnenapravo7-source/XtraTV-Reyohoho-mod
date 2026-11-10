package com.github.andreyasadchy.xtra.util

import org.junit.Assert.assertEquals
import org.junit.Test

class TvAutoQualityPolicyTest {
    @Test fun tvAutoDoesNotJumpTo1440p() { assertEquals(1080, TvAutoQualityPolicy.maxHeight(true, true)) }
    @Test fun manualHigherQualityRemainsAvailable() { assertEquals(Int.MAX_VALUE, TvAutoQualityPolicy.maxHeight(true, false, 720)) }
    @Test fun nonTvAutoIsUnchanged() { assertEquals(Int.MAX_VALUE, TvAutoQualityPolicy.maxHeight(false, true)) }
    @Test fun recoveryCanReduceButNotRaiseTvCeiling() {
        assertEquals(720, TvAutoQualityPolicy.maxHeight(true, true, 720))
        assertEquals(1080, TvAutoQualityPolicy.maxHeight(true, true, 1440))
    }
}
