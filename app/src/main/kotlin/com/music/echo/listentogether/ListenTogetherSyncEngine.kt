package iad1tya.echo.music.listentogether

import kotlin.math.abs

class ListenTogetherSyncEngine {

    enum class SyncDecision {
        DO_NOTHING,
        SEEK,
        FULL_SYNC
    }

    private var lastProcessedRevision: Long = -1L
    private var lastSeekTimeMs: Long = 0L

    fun shouldProcessEvent(eventRevision: Long?, isHost: Boolean): Boolean {
        if (isHost) return false // Host ignores remote events

        // If event has a revision, strictly order by it
        if (eventRevision != null) {
            if (eventRevision <= lastProcessedRevision) {
                return false // Stale or duplicate
            }
            lastProcessedRevision = eventRevision
            return true
        }

        // Fallback for older servers/clients without revision
        return true
    }
    
    fun setLastProcessedRevision(revision: Long) {
        lastProcessedRevision = revision
    }

    fun reset() {
        lastProcessedRevision = -1L
        lastSeekTimeMs = 0L
    }

    fun calculateDriftCorrection(
        localTrackId: String?,
        remoteTrackId: String?,
        localPosition: Long,
        localIsPlaying: Boolean,
        remotePosition: Long,
        remoteIsPlaying: Boolean,
        remoteLastUpdate: Long,
        now: Long
    ): SyncDecision {
        if (localTrackId != remoteTrackId) return SyncDecision.FULL_SYNC
        if (localIsPlaying != remoteIsPlaying) return SyncDecision.FULL_SYNC

        val expectedRemotePosition = if (remoteIsPlaying) {
            remotePosition + (now - remoteLastUpdate)
        } else {
            remotePosition
        }

        val drift = abs(localPosition - expectedRemotePosition)

        return when {
            drift < 500L -> SyncDecision.DO_NOTHING
            drift < 3000L -> {
                // Debounce seeks to avoid seek loops from rapid heartbeats
                if (now - lastSeekTimeMs > 2000L) {
                    lastSeekTimeMs = now
                    SyncDecision.SEEK
                } else {
                    SyncDecision.DO_NOTHING
                }
            }
            else -> SyncDecision.FULL_SYNC
        }
    }
}
