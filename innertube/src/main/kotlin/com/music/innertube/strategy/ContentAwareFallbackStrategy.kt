package com.music.innertube.strategy

import com.music.innertube.models.YouTubeClient

data class ContentHints(
    val isExplicit: Boolean? = null,
    val isKidsContent: Boolean? = null,
    val isLive: Boolean? = null,
    val isUploaded: Boolean? = null,
)

class ContentAwareFallbackStrategy {
    fun resolveClients(hints: ContentHints): List<YouTubeClient> =
        when {
            hints.isUploaded == true -> uploadedClients
            hints.isLive == true -> liveClients
            hints.isKidsContent == true -> kidsClients
            hints.isExplicit == true -> explicitClients
            else -> defaultClients
        }

    private companion object {
        val uploadedClients = listOf(
            YouTubeClient.WEB_REMIX,
            YouTubeClient.WEB_CREATOR,
            YouTubeClient.TVHTML5_SIMPLY,
        )

        val defaultClients = listOf(
            YouTubeClient.VISIONOS,
            YouTubeClient.VISIONOS_0_1,
            YouTubeClient.WEB_CREATOR,
            YouTubeClient.TVHTML5_SIMPLY,
        )

        val explicitClients = listOf(
            YouTubeClient.VISIONOS,
            YouTubeClient.VISIONOS_0_1,
            YouTubeClient.WEB_CREATOR,
            YouTubeClient.WEB_REMIX,
        )

        val kidsClients = listOf(
            YouTubeClient.VISIONOS,
            YouTubeClient.VISIONOS_0_1,
            YouTubeClient.WEB_CREATOR,
            YouTubeClient.TVHTML5_SIMPLY,
        )

        val liveClients = listOf(
            YouTubeClient.WEB_REMIX,
            YouTubeClient.WEB_CREATOR,
            YouTubeClient.TVHTML5_SIMPLY,
        )
    }
}
