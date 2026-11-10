package com.github.andreyasadchy.xtra.util

import org.junit.Assert.assertEquals
import org.junit.Test

class SavedTabPolicyTest {
    @Test fun migratesDownloadSelectionToBookmarks() {
        assertEquals(listOf("0:1:1", "2:0:1"), SavedTabPolicy.normalize("0:0:1,1:1:1,2:0:1"))
    }
    @Test fun preservesFilterOrderAndSelection() {
        assertEquals(listOf("2:1:1", "0:0:1"), SavedTabPolicy.normalize("2:1:1,1:0:1,0:0:1"))
    }
    @Test fun ignoresMalformedAndDuplicateTabs() {
        assertEquals(listOf("0:1:1", "2:0:1"), SavedTabPolicy.normalize("bad,9:1:1,0:1:1,0:0:0,2:8:1"))
    }
    @Test fun doesNotUnhideUserHiddenTabs() {
        assertEquals(listOf("0:0:0", "2:1:1"), SavedTabPolicy.normalize("0:1:0,2:0:1"))
    }
    @Test fun missingSettingsUseDefaults() {
        assertEquals(listOf("0:1:1", "2:0:1"), SavedTabPolicy.normalize(null))
    }
}
