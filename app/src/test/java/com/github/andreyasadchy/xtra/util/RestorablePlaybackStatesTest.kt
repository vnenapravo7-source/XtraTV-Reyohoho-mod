package com.github.andreyasadchy.xtra.util

import com.github.andreyasadchy.xtra.model.PlaybackState
import com.github.andreyasadchy.xtra.ui.player.BasePlaybackService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RestorablePlaybackStatesTest {
    @Test fun preservesOnlineQueueOrder() {
        val states = listOf(PlaybackState(type = BasePlaybackService.STREAM), PlaybackState(type = BasePlaybackService.VIDEO), PlaybackState(type = BasePlaybackService.CLIP))
        assertEquals(states, RestorablePlaybackStates.filter(states))
    }
    @Test fun mediaButtonCannotRestoreRemovedOfflineMode() {
        assertTrue(RestorablePlaybackStates.filter(listOf(PlaybackState(type = BasePlaybackService.OFFLINE_VIDEO))).isEmpty())
    }
    @Test fun legacyOfflineIdIsBlockedEvenWithoutType() {
        val online = PlaybackState(type = BasePlaybackService.STREAM)
        assertEquals(listOf(online), RestorablePlaybackStates.filter(listOf(PlaybackState(offlineVideoId = 4), online)))
    }
}
