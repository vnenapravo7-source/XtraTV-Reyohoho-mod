package com.github.andreyasadchy.xtra.util

import com.github.andreyasadchy.xtra.model.PlaybackState
import com.github.andreyasadchy.xtra.ui.player.BasePlaybackService

/** Also covers media-button/service restores that do not pass through the activity. */
object RestorablePlaybackStates {
    fun filter(states: List<PlaybackState>): List<PlaybackState> = states.filter {
        it.type != BasePlaybackService.OFFLINE_VIDEO && it.offlineVideoId == null
    }
}
