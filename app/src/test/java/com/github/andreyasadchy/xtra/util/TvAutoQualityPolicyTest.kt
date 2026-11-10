package com.github.andreyasadchy.xtra.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test

class TvAutoQualityPolicyTest {
    @Test fun autoHasNo1080pCeiling() { assertEquals(Int.MAX_VALUE, TvAutoQualityPolicy.maxHeight(true)) }
    @Test fun manualIgnoresRecoveryCap() { assertEquals(Int.MAX_VALUE, TvAutoQualityPolicy.maxHeight(false, 720)) }
    @Test fun recoveryStillReducesQualityAfterBuffering() { assertEquals(720, TvAutoQualityPolicy.maxHeight(true, 720)) }
    @Test fun recoveryCanReturnTo1440p() { assertEquals(1440, TvAutoQualityPolicy.maxHeight(true, 1440)) }
    @Test fun tvPrefersBestOnBothNetworkTypes() {
        assertTrue(TvAutoQualityPolicy.preferHighest(true, true))
        assertTrue(TvAutoQualityPolicy.preferHighest(true, false))
    }
    @Test fun nonTvNetworkPolicyIsPreserved() {
        assertTrue(TvAutoQualityPolicy.preferHighest(false, true))
        assertFalse(TvAutoQualityPolicy.preferHighest(false, false))
    }
}
