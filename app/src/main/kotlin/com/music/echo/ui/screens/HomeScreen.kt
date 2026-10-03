package iad1tya.echo.music.ui.screens
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll


import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.animateFloat
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.request.ImageRequest
import com.music.innertube.models.AlbumItem
import com.music.innertube.models.ArtistItem
import com.music.innertube.models.PlaylistItem
import com.music.innertube.models.SongItem
import com.music.innertube.models.WatchEndpoint
import com.music.innertube.models.YTItem
import com.music.innertube.utils.completed
import com.music.innertube.utils.parseCookieString
import com.music.innertube.YouTube
import iad1tya.echo.music.constants.GridItemSize
import iad1tya.echo.music.constants.GridItemsSizeKey
import iad1tya.echo.music.constants.GridThumbnailHeight
import iad1tya.echo.music.constants.InnerTubeCookieKey
import iad1tya.echo.music.constants.ListItemHeight
import iad1tya.echo.music.constants.ListThumbnailSize
import iad1tya.echo.music.constants.RandomizeHomeOrderKey
import iad1tya.echo.music.constants.ShowSpeedDialKey
import iad1tya.echo.music.constants.SmallGridThumbnailHeight
import iad1tya.echo.music.constants.ThumbnailCornerRadius
import iad1tya.echo.music.db.entities.Album
import iad1tya.echo.music.db.entities.Artist
import iad1tya.echo.music.db.entities.LocalItem
import iad1tya.echo.music.db.entities.Playlist
import iad1tya.echo.music.db.entities.PlaylistEntity
import iad1tya.echo.music.db.entities.PlaylistSongMap
import iad1tya.echo.music.db.entities.Song
import iad1tya.echo.music.extensions.toMediaItem
import iad1tya.echo.music.LocalDatabase
import iad1tya.echo.music.LocalPlayerAwareWindowInsets
import iad1tya.echo.music.LocalPlayerConnection
import iad1tya.echo.music.models.toMediaMetadata
import iad1tya.echo.music.playback.queues.ListQueue
import iad1tya.echo.music.playback.queues.LocalAlbumRadio
import iad1tya.echo.music.playback.queues.YouTubeAlbumRadio
import iad1tya.echo.music.playback.queues.YouTubeQueue
import iad1tya.echo.music.R
import iad1tya.echo.music.ui.component.AlbumGridItem
import iad1tya.echo.music.ui.component.ArtistGridItem
import iad1tya.echo.music.ui.component.ChipsRow
import iad1tya.echo.music.ui.component.HideOnScrollFAB
import iad1tya.echo.music.ui.component.LocalBottomSheetPageState
import iad1tya.echo.music.ui.component.LocalMenuState
import iad1tya.echo.music.ui.component.NavigationTitle
import iad1tya.echo.music.ui.component.RandomizeGridItem
import iad1tya.echo.music.ui.component.shimmer.GridItemPlaceHolder
import iad1tya.echo.music.ui.component.shimmer.ShimmerHost
import iad1tya.echo.music.ui.component.shimmer.TextPlaceholder
import iad1tya.echo.music.ui.component.SongGridItem
import iad1tya.echo.music.ui.component.SongListItem
import iad1tya.echo.music.ui.component.SpeedDialGridItem
import iad1tya.echo.music.ui.component.YouTubeGridItem
import iad1tya.echo.music.ui.component.YouTubeListItem
import iad1tya.echo.music.ui.menu.AlbumMenu
import iad1tya.echo.music.ui.menu.ArtistMenu
import iad1tya.echo.music.ui.menu.SongMenu
import iad1tya.echo.music.ui.menu.YouTubeAlbumMenu
import iad1tya.echo.music.ui.menu.YouTubeArtistMenu
import iad1tya.echo.music.ui.menu.YouTubePlaylistMenu
import iad1tya.echo.music.ui.menu.YouTubeSongMenu
import iad1tya.echo.music.ui.utils.SnapLayoutInfoProvider
import iad1tya.echo.music.ui.utils.resize
import iad1tya.echo.music.utils.listItemShape
import iad1tya.echo.music.utils.rememberEnumPreference
import iad1tya.echo.music.utils.rememberPreference
import iad1tya.echo.music.viewmodels.CommunityPlaylistItem
import iad1tya.echo.music.viewmodels.HomeViewModel
import kotlin.math.min
import kotlin.random.Random
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import iad1tya.echo.music.viewmodels.DailyDiscoverItem

private fun NavController.navigateToPlaylistItem(playlist: PlaylistItem) {
    when (val playlistId = playlist.id.removePrefix("VL")) {
        "LM" -> navigate("auto_playlist/liked")
        "SE" -> navigate("auto_playlist/downloaded")
        else -> navigate("online_playlist/$playlistId")
    }
}

sealed class HomeSection(val id: String, val baseWeight: Int) {
    data object SpeedDial : HomeSection("speed_dial", 100)
    data object AiRecommendations : HomeSection("ai_recommendations", 95)
    data object QuickPicks : HomeSection("quick_picks", 90)
    data object DailyDiscover : HomeSection("daily_discover", 80)
    data object KeepListening : HomeSection("keep_listening", 50)
    data object AccountPlaylists : HomeSection("account_playlists", 40)
    data object ForgottenFavorites : HomeSection("forgotten_favorites", 30)
    data object FromTheCommunity : HomeSection("from_the_community", 20)
    data class SimilarRecommendation(val index: Int) : HomeSection("similar_recommendation_$index", 10)
    data class HomePageSection(val index: Int) : HomeSection("home_page_section_$index", 10)
    data object MoodAndGenres : HomeSection("mood_and_genres", 5)
}

@Composable
fun CommunityPlaylistCard(
    item: CommunityPlaylistItem,
    onClick: () -> Unit,
    onSongClick: (SongItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val database = LocalDatabase.current
    val playerConnection = LocalPlayerConnection.current
    val scope = rememberCoroutineScope()
    val isDark = isSystemInDarkTheme()

    val containerColor = if (isDark) {
        MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    }

    val dbPlaylist by database.playlistByBrowseId(item.playlist.id).collectAsState(initial = null)
    val isBookmarked = dbPlaylist?.playlist?.bookmarkedAt != null

    Card(
        modifier = modifier
            .width(320.dp)
            .height(420.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        shape = RoundedCornerShape(28.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(modifier = Modifier.weight(1f)) {
                            AsyncImage(
                                model = item.songs.getOrNull(0)?.thumbnail?.resize(544, 544),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                            )
                            AsyncImage(
                                model = item.songs.getOrNull(1)?.thumbnail?.resize(544, 544),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                            )
                        }
                        Row(modifier = Modifier.weight(1f)) {
                            AsyncImage(
                                model = item.songs.getOrNull(2)?.thumbnail?.resize(544, 544),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                            )
                            AsyncImage(
                                model = item.songs.getOrNull(3)?.thumbnail?.resize(544, 544),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = item.playlist.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.playlist.author?.name ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        maxLines = 1
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                item.songs.take(3).forEach { song ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .combinedClickable(onClick = { onSongClick(song) }),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AsyncImage(
                            model = song.thumbnail.resize(544, 544),
                            contentDescription = null,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = song.title,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                            Text(
                                text = song.artists.joinToString(", ") { it.name },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
            ) {
                IconButton(
                    onClick = {
                        item.playlist.playEndpoint?.let {
                            playerConnection?.playQueue(YouTubeQueue(it))
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_widget_play),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(
                    onClick = {
                        item.playlist.radioEndpoint?.let {
                            playerConnection?.playQueue(YouTubeQueue(it))
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.radio),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(
                    onClick = {
                        scope.launch(Dispatchers.IO) {
                            if (dbPlaylist?.playlist == null) {
                                database.transaction {
                                    val playlistEntity = PlaylistEntity(
                                        name = item.playlist.title,
                                        browseId = item.playlist.id,
                                        thumbnailUrl = item.playlist.thumbnail,
                                        remoteSongCount = item.playlist.songCountText?.split(" ")?.firstOrNull()?.toIntOrNull(),
                                        playEndpointParams = item.playlist.playEndpoint?.params,
                                        shuffleEndpointParams = item.playlist.shuffleEndpoint?.params,
                                        radioEndpointParams = item.playlist.radioEndpoint?.params
                                    ).toggleLike()
                                    insert(playlistEntity)
                                    scope.launch(Dispatchers.IO) {
                                        item.songs.ifEmpty {
                                            YouTube.playlist(item.playlist.id).completed()
                                                .getOrNull()?.songs.orEmpty()
                                        }.map { it.toMediaMetadata() }
                                            .onEach(::insert)
                                            .mapIndexed { index, song ->
                                                PlaylistSongMap(
                                                    songId = song.id,
                                                    playlistId = playlistEntity.id,
                                                    position = index,
                                                    setVideoId = song.setVideoId
                                                )
                                            }
                                            .forEach(::insert)
                                    }
                                }
                            } else {
                                database.transaction {
                                    val currentPlaylist = dbPlaylist!!.playlist
                                    update(currentPlaylist.toggleLike())
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        painter = painterResource(if (isBookmarked) R.drawable.library_add_check else R.drawable.library_add),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DailyDiscoverCard(
    dailyDiscover: iad1tya.echo.music.viewmodels.DailyDiscoverItem,
    onClick: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val database = LocalDatabase.current
    val playCount by database.getLifetimePlayCount(dailyDiscover.recommendation.id).collectAsState(initial = 0)
    val menuState = LocalMenuState.current
    val haptic = LocalHapticFeedback.current

    val song = dailyDiscover.recommendation as? SongItem
    val playsString = stringResource(R.string.plays)

    Card(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(28.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (song != null) {
                        menuState.show {
                            YouTubeSongMenu(
                                song = song,
                                navController = navController,
                                onDismiss = { menuState.dismiss() }
                            )
                        }
                    }
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(28.dp)
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(dailyDiscover.recommendation.thumbnail?.resize(1200, 1200))
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )

            if (maxWidth > 200.dp) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.3f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.6f),
                                    Color.Black.copy(alpha = 0.9f)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = dailyDiscover.recommendation.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        Text(
                            text = buildString {
                                append((dailyDiscover.recommendation as? SongItem)?.artists?.joinToString(", ") { it.name } ?: "")
                                if (playCount > 0) {
                                    append(" • $playCount $playsString")
                                }
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }

                    val messages = listOf(
                        R.string.daily_discover_sounds_like,
                        R.string.daily_discover_because_you_listen_to,
                        R.string.daily_discover_similar_to,
                        R.string.daily_discover_based_on,
                        R.string.daily_discover_for_fans_of
                    )
                    val messageRes = remember(dailyDiscover.seed.id) {
                        messages[kotlin.math.abs(dailyDiscover.seed.id.hashCode()) % messages.size]
                    }

                    Text(
                        text = stringResource(messageRes, "${dailyDiscover.seed.title} • ${dailyDiscover.seed.artists.joinToString(", ") { it.name }}"),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                        // This sits on a dark artwork scrim. onSurface becomes dark in
                        // light theme and makes the footer unreadable.
                        color = Color.White.copy(alpha = 0.78f),
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

enum class PixelScene {
    MORNING,
    DAY,
    AFTERNOON,
    EVENING,
    NIGHT
}

/** The Home screen's video background greeting card. */
@Composable
private fun AnimePixelGreetingCard(userName: String) {
    var now by remember { mutableStateOf(LocalDateTime.now()) }
    LaunchedEffect(Unit) {
        while (true) { now = LocalDateTime.now(); kotlinx.coroutines.delay(1_000L) }
    }
    val scene = when {
        now.hour in 5..9 -> PixelScene.MORNING
        now.hour in 10..15 -> PixelScene.DAY
        now.hour == 16 || (now.hour == 17 && now.minute < 30) -> PixelScene.AFTERNOON
        (now.hour == 17 && now.minute >= 30) || now.hour in 18..20 -> PixelScene.EVENING
        else -> PixelScene.NIGHT
    }
    val greeting = when (scene) { PixelScene.MORNING -> "GOOD MORNING"; PixelScene.DAY -> "GOOD AFTERNOON"; PixelScene.AFTERNOON -> "GOOD AFTERNOON"; PixelScene.EVENING -> "GOOD EVENING"; PixelScene.NIGHT -> "GOOD NIGHT" }
    val clock = now.format(DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault()))
    val date = now.format(DateTimeFormatter.ofPattern("EEE, d MMM yyyy", Locale.getDefault()))
    val shape = CutCornerShape(6.dp, 26.dp, 6.dp, 26.dp)
    
    // Fallback accent color for text
    val accent = when (scene) {
        PixelScene.MORNING -> Color(0xFFFFD166)
        PixelScene.DAY -> Color(0xFFFFE066)
        PixelScene.AFTERNOON -> Color(0xFFFFB566)
        PixelScene.EVENING -> Color(0xFFFF9F68)
        PixelScene.NIGHT -> Color(0xFFF5E8B5)
    }

    Box(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp).height(250.dp).clip(shape)) {
        val bgRes = when (scene) {
            PixelScene.MORNING -> iad1tya.echo.music.R.raw.bg_morning
            PixelScene.DAY -> iad1tya.echo.music.R.raw.bg_day
            PixelScene.AFTERNOON -> iad1tya.echo.music.R.raw.bg_afternoon
            PixelScene.EVENING -> iad1tya.echo.music.R.raw.bg_evening
            PixelScene.NIGHT -> iad1tya.echo.music.R.raw.bg_night
        }

        LoopingVideoBackground(
            videoRes = bgRes,
            modifier = Modifier.fillMaxSize()
        )

        Box(Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(Color.Black.copy(alpha = 0.35f), Color.Black.copy(alpha = 0.15f), Color.Transparent))))
        Column(Modifier.align(Alignment.BottomStart).padding(start = 16.dp, bottom = 15.dp).fillMaxWidth(.76f)) {
            Text(greeting, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.5.sp, color = accent)
            Text("Hello, ${userName.ifBlank { "Guest" }}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text("Let the music set the mood.", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = .82f), maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.clip(CutCornerShape(6.dp)).background(Color.Black.copy(alpha = .48f)).border(1.dp, accent.copy(alpha = .55f), CutCornerShape(6.dp)).padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) { Text(clock, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White); Spacer(Modifier.width(8.dp)); Text(date, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = .7f), maxLines = 1) }
        }
        Row(Modifier.align(Alignment.TopStart).padding(14.dp), horizontalArrangement = Arrangement.spacedBy(5.dp)) { repeat(5) { Box(Modifier.size(if (it == 0) 8.dp else 5.dp).background(accent.copy(alpha = if (it == 0) .95f else .45f))) } }
        Box(Modifier.align(Alignment.TopEnd).padding(14.dp).size(6.dp).background(accent))
    }
}

@Composable
private fun MostPlayedHeroCarousel(
    songs: List<Song>,
    playCounts: Map<String, Long>,
    onPlay: (Song) -> Unit,
    onLongClick: (Song) -> Unit,
) {
    if (songs.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { songs.size })

    LaunchedEffect(songs.map { it.id }) {
        if (songs.size > 1) {
            while (true) {
                kotlinx.coroutines.delay(6_000L)
                if (!pagerState.isScrollInProgress) {
                    pagerState.animateScrollToPage((pagerState.currentPage + 1) % songs.size)
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 12.dp),
            pageSpacing = 10.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
        ) { page ->
            val song = songs[page]
            val playCount = playCounts[song.id] ?: 0L
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CutCornerShape(6.dp, 26.dp, 6.dp, 26.dp))
                    .combinedClickable(
                        onClick = { onPlay(song) },
                        onLongClick = { onLongClick(song) },
                    )
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(song.thumbnailUrl?.resize(1080, 1080))
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .networkCachePolicy(CachePolicy.ENABLED)
                        .crossfade(true)
                        .build(),
                    contentDescription = song.song.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Black.copy(alpha = 0.12f),
                                    Color.Black.copy(alpha = 0.32f),
                                    Color.Black.copy(alpha = 0.90f),
                                )
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Text(
                        text = "MOST PLAYED ${page + 1}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        // The image behind this label is dark in both themes; a dynamic
                        // light-theme primary can be too low-contrast here.
                        color = Color.White.copy(alpha = 0.92f),
                        letterSpacing = 1.5.sp,
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = song.song.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = song.artists.joinToString { it.name },
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.82f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (playCount > 0) "$playCount ${if (playCount == 1L) "play" else "plays"}" else "Tap to play",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White.copy(alpha = 0.76f),
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = { onPlay(song) },
                            modifier = Modifier
                                .size(52.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_widget_play),
                                contentDescription = "Play ${song.song.title}",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(25.dp),
                            )
                        }
                    }
                }
            }
        }

        if (songs.size > 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                repeat(songs.size) { index ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (pagerState.currentPage == index) 8.dp else 5.dp)
                            .background(
                                MaterialTheme.colorScheme.primary.copy(
                                    alpha = if (pagerState.currentPage == index) 0.95f else 0.45f
                                )
                            )
                    )
                }
            }
        }
    }
}
// ─────────────────────────────────────────────────────────────────────────
// DISCOVERY SECTION HELPERS
// ─────────────────────────────────────────────────────────────────────────

@Composable
private fun HomeSectionHeader(
    title: String,
    onSeeAll: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f),
        )
        if (onSeeAll != null) {
            Text(
                text = "See all",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .combinedClickable(onClick = onSeeAll)
                    .padding(start = 8.dp),
            )
        }
    }
}

@Composable
private fun CompactMusicCard(
    thumbnailUrl: String?,
    title: String,
    subtitle: String,
    isActive: Boolean,
    isPlaying: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = CutCornerShape(topStart = 4.dp, topEnd = 14.dp, bottomEnd = 4.dp, bottomStart = 14.dp)
    Column(
        modifier = modifier
            .width(148.dp)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
    ) {
        Box(
            modifier = Modifier
                .size(148.dp)
                .clip(cardShape)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(thumbnailUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
            // Gradient for text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.55f))
                        )
                    )
            )
            if (isActive && isPlaying) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(22.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.volume_up),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(12.dp),
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun CompactAlbumCard(
    thumbnailUrl: String?,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = CutCornerShape(topStart = 4.dp, topEnd = 14.dp, bottomEnd = 4.dp, bottomStart = 14.dp)
    Column(
        modifier = modifier
            .width(148.dp)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
    ) {
        Box(
            modifier = Modifier
                .size(148.dp)
                .clip(cardShape)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(thumbnailUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

@Composable
private fun MostPlayedHero(
    song: Song,
    playCount: Long,
    onPlay: () -> Unit,
    onLongClick: () -> Unit,
) {
    val shape = CutCornerShape(topStart = 6.dp, topEnd = 28.dp, bottomEnd = 6.dp, bottomStart = 28.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .height(250.dp)
            .clip(shape)
            .combinedClickable(
                onClick = onPlay,
                onLongClick = onLongClick,
            )
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(song.thumbnailUrl?.resize(1080, 1080))
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .networkCachePolicy(CachePolicy.ENABLED)
                .crossfade(true)
                .build(),
            contentDescription = song.song.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.18f),
                            Color.Black.copy(alpha = 0.32f),
                            Color.Black.copy(alpha = 0.88f),
                        )
                    )
                )
        )

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(4) { index ->
                Box(
                    modifier = Modifier
                        .size(if (index == 0) 8.dp else 5.dp)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(
                                alpha = if (index == 0) 0.9f else 0.55f
                            )
                        )
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = "MOST PLAYED",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.5.sp,
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = song.song.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = song.artists.joinToString { it.name },
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.82f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text = playCount.toString() + if (playCount == 1L) " play" else " plays",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.76f),
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = onPlay,
                    modifier = Modifier
                        .size(52.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            CircleShape
                        )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_widget_play),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(25.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val menuState = LocalMenuState.current
    val bottomSheetPageState = LocalBottomSheetPageState.current
    val database = LocalDatabase.current
    val playerConnection = LocalPlayerConnection.current ?: return

    val mostPlayedStats by database.mostPlayedSongStats().collectAsState(initial = null)
    val mostPlayedSong by remember(mostPlayedStats?.songId) {
        database.song(mostPlayedStats?.songId)
    }.collectAsState(initial = null)
    val mostPlayedSongs by database.mostPlayedSongs(0L, limit = 5).collectAsState(initial = emptyList())
    val mostPlayedSongsStats by database.mostPlayedSongsStats(0L, limit = 5).collectAsState(initial = emptyList())
    val mostPlayedPlayCounts = remember(mostPlayedSongsStats) {
        mostPlayedSongsStats.associate { it.id to it.songCountListened.toLong() }
    }
    val haptic = LocalHapticFeedback.current

    val isPlaying by playerConnection.isEffectivelyPlaying.collectAsState()
    val mediaMetadata by playerConnection.mediaMetadata.collectAsState()

    val quickPicks by viewModel.quickPicks.collectAsState()
    val aiRecommendedPlaylist by viewModel.aiRecommendedPlaylist.collectAsState()
    val forgottenFavorites by viewModel.forgottenFavorites.collectAsState()
    val keepListening by viewModel.keepListening.collectAsState()
    val similarRecommendations by viewModel.similarRecommendations.collectAsState()
    val accountPlaylists by viewModel.accountPlaylists.collectAsState()
    val homePage by viewModel.homePage.collectAsState()
    val explorePage by viewModel.explorePage.collectAsState()
    val dailyDiscover by viewModel.dailyDiscover.collectAsState()
    val communityPlaylists by viewModel.communityPlaylists.collectAsState()

    val allLocalItems by viewModel.allLocalItems.collectAsState()
    val allYtItems by viewModel.allYtItems.collectAsState()
    val speedDialItems by viewModel.speedDialItems.collectAsState()
    val selectedChip by viewModel.selectedChip.collectAsState()

    // Derived discovery data
    val recentEvents by database.events().collectAsState(initial = emptyList())
    val recentlyPlayedSongs = remember(recentEvents) {
        recentEvents
            .map { it.song }
            .distinctBy { it.id }
            .take(12)
    }
    val mostPlayedCompact = remember(quickPicks) {
        quickPicks?.distinctBy { it.id }?.take(10) ?: emptyList()
    }
    val newReleaseAlbums = remember(explorePage) {
        explorePage?.newReleaseAlbums?.distinctBy { it.id }?.take(12) ?: emptyList()
    }
    val discoverSongs = remember(dailyDiscover) {
        dailyDiscover?.map { it.recommendation }?.distinctBy { it.id }?.take(10) ?: emptyList()
    }

    val isLoading: Boolean by viewModel.isLoading.collectAsState()
    val isMoodAndGenresLoading = isLoading && explorePage?.moodAndGenres == null
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val isRandomizing by viewModel.isRandomizing.collectAsState()
    val pullRefreshState = rememberPullToRefreshState()

    val quickPicksLazyGridState = rememberLazyGridState()
    val forgottenFavoritesLazyGridState = rememberLazyGridState()

    val accountName by viewModel.accountName.collectAsState()
    val accountImageUrl by viewModel.accountImageUrl.collectAsState()
    val innerTubeCookie by rememberPreference(InnerTubeCookieKey, "")
    val (randomizeHomeOrder) = rememberPreference(RandomizeHomeOrderKey, true)
    val (showSpeedDial) = rememberPreference(ShowSpeedDialKey, true)


    val isLoggedIn = remember(innerTubeCookie) {
        "SAPISID" in parseCookieString(innerTubeCookie)
    }
    val url = if (isLoggedIn) accountImageUrl else null

    val scope = rememberCoroutineScope()
    
    var randomizeJob by remember { mutableStateOf<kotlinx.coroutines.Job?>(null) }

    val lazylistState = rememberLazyListState()
    val gridItemSize by rememberEnumPreference(GridItemsSizeKey, GridItemSize.BIG)
    val currentGridHeight = if (gridItemSize == GridItemSize.BIG) GridThumbnailHeight else SmallGridThumbnailHeight
    val backStackEntry by navController.currentBackStackEntryAsState()
    val scrollToTop =
        backStackEntry?.savedStateHandle?.getStateFlow("scrollToTop", false)?.collectAsState()


    var randomSeed by rememberSaveable { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            randomSeed = System.currentTimeMillis()
        }
    }

    val foundInSettings = stringResource(R.string.found_in_settings_content)

    LaunchedEffect(scrollToTop?.value) {
        if (scrollToTop?.value == true) {
            lazylistState.animateScrollToItem(0)
            backStackEntry?.savedStateHandle?.set("scrollToTop", false)
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { lazylistState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                val len = lazylistState.layoutInfo.totalItemsCount
                if (lastVisibleIndex != null && lastVisibleIndex >= len - 3) {
                    viewModel.loadMoreYouTubeItems(homePage?.continuation)
                }
            }
    }

    NetworkReload(
        onReload = viewModel::refresh
    )

    if (selectedChip != null) {
        BackHandler {
            
            viewModel.toggleChip(selectedChip)
        }
    }

    val localGridItem: @Composable (LocalItem) -> Unit = {
        when (it) {
            is Song -> SongGridItem(
                song = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .combinedClickable(
                        onClick = {
                            if (it.id == mediaMetadata?.id) {
                                playerConnection.togglePlayPause()
                            } else {
                                playerConnection.playQueue(
                                    YouTubeQueue.radio(it.toMediaMetadata()),
                                )
                            }
                        },
                        onLongClick = {
                            haptic.performHapticFeedback(
                                HapticFeedbackType.LongPress,
                            )
                            menuState.show {
                                SongMenu(
                                    originalSong = it,
                                    navController = navController,
                                    onDismiss = menuState::dismiss,
                                )
                            }
                        },
                    ),
                isActive = it.id == mediaMetadata?.id,
                isPlaying = isPlaying,
            )

            is Album -> AlbumGridItem(
                album = it,
                isActive = it.id == mediaMetadata?.album?.id,
                isPlaying = isPlaying,
                coroutineScope = scope,
                modifier = Modifier
                    .fillMaxWidth()
                    .combinedClickable(
                        onClick = {
                            navController.navigate("album/${it.id}")
                        },
                        onLongClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            menuState.show {
                                AlbumMenu(
                                    originalAlbum = it,
                                    navController = navController,
                                    onDismiss = menuState::dismiss
                                )
                            }
                        }
                    )
            )

            is Artist -> ArtistGridItem(
                artist = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .combinedClickable(
                        onClick = {
                            navController.navigate("artist/${it.id}")
                        },
                        onLongClick = {
                            haptic.performHapticFeedback(
                                HapticFeedbackType.LongPress,
                            )
                            menuState.show {
                                ArtistMenu(
                                    originalArtist = it,
                                    coroutineScope = scope,
                                    onDismiss = menuState::dismiss,
                                )
                            }
                        },
                    ),
            )

            is Playlist -> {}
        }
    }

    val ytGridItem: @Composable (YTItem) -> Unit = { item ->
        YouTubeGridItem(
            item = item,
            isActive = item.id in listOf(mediaMetadata?.album?.id, mediaMetadata?.id),
            isPlaying = isPlaying,
            coroutineScope = scope,
            thumbnailRatio = 1f,
            modifier = Modifier
                .combinedClickable(
                    onClick = {
                        when (item) {
                            is SongItem -> playerConnection.playQueue(
                                YouTubeQueue(
                                    item.endpoint ?: WatchEndpoint(
                                        videoId = item.id
                                    ), item.toMediaMetadata()
                                )
                            )

                            is AlbumItem -> navController.navigate("album/${item.id}")
                            is ArtistItem -> navController.navigate("artist/${item.id}")
                            is PlaylistItem -> navController.navigateToPlaylistItem(item)
                        }
                    },
                    onLongClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        menuState.show {
                            when (item) {
                                is SongItem -> YouTubeSongMenu(
                                    song = item,
                                    navController = navController,
                                    onDismiss = menuState::dismiss
                                )

                                is AlbumItem -> YouTubeAlbumMenu(
                                    albumItem = item,
                                    navController = navController,
                                    onDismiss = menuState::dismiss
                                )

                                is ArtistItem -> YouTubeArtistMenu(
                                    artist = item,
                                    onDismiss = menuState::dismiss
                                )

                                is PlaylistItem -> YouTubePlaylistMenu(
                                    playlist = item,
                                    coroutineScope = scope,
                                    onDismiss = menuState::dismiss
                                )
                            }
                        }
                    }
                )
        )
    }

    val homeSections = remember(
        randomizeHomeOrder,
        randomSeed,
        speedDialItems,
        quickPicks,
        dailyDiscover,
        keepListening,
        accountPlaylists,
        forgottenFavorites,
        communityPlaylists,
        similarRecommendations,
        homePage?.sections,
        explorePage?.moodAndGenres,
        aiRecommendedPlaylist
    ) {
        val list = mutableListOf<HomeSection>()

        if (showSpeedDial && speedDialItems.isNotEmpty()) list.add(HomeSection.SpeedDial)
        if (aiRecommendedPlaylist != null && aiRecommendedPlaylist!!.second.isNotEmpty()) list.add(HomeSection.AiRecommendations)
        if (quickPicks?.isNotEmpty() == true) list.add(HomeSection.QuickPicks)
        if (communityPlaylists?.isNotEmpty() == true) list.add(HomeSection.FromTheCommunity)
        if (dailyDiscover?.isNotEmpty() == true) list.add(HomeSection.DailyDiscover)
        if (keepListening?.isNotEmpty() == true) list.add(HomeSection.KeepListening)
        if (accountPlaylists?.isNotEmpty() == true) list.add(HomeSection.AccountPlaylists)
        if (forgottenFavorites?.isNotEmpty() == true) list.add(HomeSection.ForgottenFavorites)

        similarRecommendations?.indices?.forEach { i ->
            list.add(HomeSection.SimilarRecommendation(i))
        }

        homePage?.sections?.indices?.forEach { i ->
            list.add(HomeSection.HomePageSection(i))
        }

        if (explorePage?.moodAndGenres != null) list.add(HomeSection.MoodAndGenres)

        if (randomizeHomeOrder) {
            list.sortedByDescending { section ->
                
                
                
                val sectionRandom = Random(randomSeed + section.id.hashCode())

                
                
                val base = when (section) {
                    HomeSection.QuickPicks -> 10000
                    HomeSection.SpeedDial,
                    HomeSection.DailyDiscover -> 500 

                    HomeSection.KeepListening,
                    HomeSection.AccountPlaylists,
                    HomeSection.ForgottenFavorites,
                    HomeSection.FromTheCommunity -> 300 

                    else -> 100 
                }

                val modifier = when (section) {
                    
                    
                    HomeSection.QuickPicks -> 0
                    HomeSection.SpeedDial,
                    HomeSection.DailyDiscover -> sectionRandom.nextInt(-200, 400)

                    
                    
                    
                    HomeSection.KeepListening,
                    HomeSection.AccountPlaylists,
                    HomeSection.ForgottenFavorites,
                    HomeSection.FromTheCommunity -> sectionRandom.nextInt(-100, 400)

                    
                    else -> sectionRandom.nextInt(-50, 50)
                }
                base + modifier
            }
        } else {
            val defaultOrder = mapOf(
                HomeSection.QuickPicks to 1000,
                HomeSection.SpeedDial to 100,
                HomeSection.FromTheCommunity to 80,
                HomeSection.DailyDiscover to 70,
                HomeSection.KeepListening to 60,
                HomeSection.AccountPlaylists to 50,
                HomeSection.ForgottenFavorites to 40,
                HomeSection.MoodAndGenres to 10
            )

            list.sortedByDescending { section ->
                when(section) {
                    is HomeSection.SimilarRecommendation -> 30 - section.index
                    is HomeSection.HomePageSection -> 20 - section.index
                    else -> defaultOrder[section] ?: 0
                }
            }
        }
    }

    LaunchedEffect(quickPicks) {
        quickPicksLazyGridState.scrollToItem(0)
    }

    LaunchedEffect(forgottenFavorites) {
        forgottenFavoritesLazyGridState.scrollToItem(0)
    }

    PullToRefreshBox(
        state = pullRefreshState,
        isRefreshing = isRefreshing,
        onRefresh = viewModel::refresh,
        indicator = {
            PullToRefreshDefaults.LoadingIndicator(
                state = pullRefreshState,
                isRefreshing = isRefreshing,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(LocalPlayerAwareWindowInsets.current.asPaddingValues()),
            )
        }
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            val horizontalLazyGridItemWidthFactor = if (maxWidth * 0.475f >= 320.dp) 0.475f else 0.9f
            val horizontalLazyGridItemWidth = maxWidth * horizontalLazyGridItemWidthFactor
            val quickPicksSnapLayoutInfoProvider = remember(quickPicksLazyGridState) {
                @OptIn(ExperimentalFoundationApi::class)
                SnapLayoutInfoProvider(
                    lazyGridState = quickPicksLazyGridState,
                    positionInLayout = { layoutSize, itemSize ->
                        (layoutSize * horizontalLazyGridItemWidthFactor / 2f - itemSize / 2f)
                    }
                )
            }
            val forgottenFavoritesSnapLayoutInfoProvider = remember(forgottenFavoritesLazyGridState) {
                @OptIn(ExperimentalFoundationApi::class)
                SnapLayoutInfoProvider(
                    lazyGridState = forgottenFavoritesLazyGridState,
                    positionInLayout = { layoutSize, itemSize ->
                        (layoutSize * horizontalLazyGridItemWidthFactor / 2f - itemSize / 2f)
                    }
                )
            }

            LazyColumn(
                state = lazylistState,
                contentPadding = LocalPlayerAwareWindowInsets.current.asPaddingValues()
            ) {
                item(key = "anime_pixel_greeting") {
                    AnimePixelGreetingCard(
                        userName = accountName.takeIf { it.isNotBlank() } ?: "Guest",
                    )
                }

                if (mostPlayedSong != null && (mostPlayedStats?.playCount ?: 0L) > 0L && mostPlayedSongs.isNotEmpty()) {
                    item(key = "most_played_hero") {
                        MostPlayedHeroCarousel(
                            songs = mostPlayedSongs,
                            playCounts = mostPlayedPlayCounts,
                            onPlay = { song ->
                                if (song.id == mediaMetadata?.id) {
                                    playerConnection.togglePlayPause()
                                } else {
                                    playerConnection.playQueue(YouTubeQueue.radio(song.toMediaMetadata()))
                                }
                            },
                            onLongClick = { song ->
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                menuState.show {
                                    SongMenu(
                                        originalSong = song,
                                        navController = navController,
                                        onDismiss = menuState::dismiss
                                    )
                                }
                            }
                        )
                    }
                }

                item {
                    ChipsRow(
                        chips = homePage?.chips?.filter { 
                            !it.title.equals("Podcasts", ignoreCase = true) && 
                            !it.title.equals("Uploaded", ignoreCase = true)
                        }?.map { it to it.title } ?: emptyList(),
                        currentValue = selectedChip,
                        onValueUpdate = {
                            viewModel.toggleChip(it)
                        }
                    )
                }

                if (isLoading && homePage?.chips.isNullOrEmpty()) {
                    item(key = "chips_shimmer") {
                        ShimmerHost {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                repeat(5) {
                                    TextPlaceholder(
                                        height = 30.dp,
                                        shape = RoundedCornerShape(16.dp),
                                        modifier = Modifier.width(72.dp)
                                    )
                                }
                            }
                        }
                    }
                }


                homeSections.forEach { section ->
                    when (section) {
                        HomeSection.SpeedDial -> {
                            speedDialItems.takeIf { it.isNotEmpty() }?.let { items ->
                                item(key = "speed_dial_title") {
                                    NavigationTitle(
                                        title = stringResource(R.string.speed_dial),
                                        modifier = Modifier.animateItem()
                                    )
                                }

                                item(key = "speed_dial_list") {
                                    val targetItemSize = 160.dp
                                    val availableWidth = maxWidth - 32.dp
                                    val columns = (availableWidth / targetItemSize).toInt().coerceAtLeast(3)
                                    val rows = if (columns >= 6) 1 else if (columns >= 4) 2 else 3
                                    val itemsPerPage = columns * rows
                                    val itemWidth = availableWidth / columns

                                    val pagerState = rememberPagerState(pageCount = { (items.size + itemsPerPage - 1) / itemsPerPage })

                                    Column(
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .animateItem(),
                                    ) {
                                        HorizontalPager(
                                            state = pagerState,
                                            contentPadding = PaddingValues(horizontal = 16.dp),
                                            pageSpacing = 16.dp,
                                            modifier =
                                                Modifier
                                                    .fillMaxWidth()
                                                    .height(itemWidth * rows),
                                        ) { page ->
                                            val pageStartIndex = page * itemsPerPage
                                            val pageItems = items.drop(pageStartIndex).take(itemsPerPage)

                                            Column(modifier = Modifier.fillMaxSize()) {
                                                for (row in 0 until rows) {
                                                    Row(modifier = Modifier.fillMaxWidth()) {
                                                        for (col in 0 until columns) {
                                                            val itemIndex = row * columns + col

                                                            val isRandomizeSlot = (page == 0 && itemIndex == itemsPerPage - 1)

                                                            if (isRandomizeSlot) {
                                                                Box(
                                                                    modifier = Modifier
                                                                        .width(itemWidth)
                                                                        .height(itemWidth)
                                                                        .padding(4.dp)
                                                                ) {
                                                                    RandomizeGridItem(
                                                                        isLoading = isRandomizing,
                                                                        onClick = {
                                                                            if (isRandomizing) {
                                                                                randomizeJob?.cancel()
                                                                            } else {
                                                                                randomizeJob = scope.launch {
                                                                                    val randomItem = viewModel.getRandomItem()
                                                                                    if (randomItem != null) {
                                                                                        when (randomItem) {
                                                                                            is SongItem -> playerConnection.playQueue(
                                                                                                YouTubeQueue(
                                                                                                    randomItem.endpoint ?: WatchEndpoint(videoId = randomItem.id),
                                                                                                    randomItem.toMediaMetadata()
                                                                                                )
                                                                                            )
                                                                                            is AlbumItem -> navController.navigate("album/${randomItem.id}")
                                                                                            is ArtistItem -> navController.navigate("artist/${randomItem.id}")
                                                                                            is PlaylistItem -> navController.navigateToPlaylistItem(randomItem)
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    )
                                                                }
                                                            } else if (itemIndex < pageItems.size) {
                                                                val item = pageItems[itemIndex]
                                                                val isPinned by database.speedDialDao.isPinned(item.id).collectAsState(initial = false)

                                                                Box(
                                                                    modifier = Modifier
                                                                        .width(itemWidth)
                                                                        .height(itemWidth)
                                                                        .padding(4.dp)
                                                                ) {
                                                                    SpeedDialGridItem(
                                                                        item = item,
                                                                        isPinned = isPinned,
                                                                        isActive = item.id in listOf(mediaMetadata?.album?.id, mediaMetadata?.id),
                                                                        isPlaying = isPlaying,
                                                                        modifier = Modifier
                                                                            .fillMaxSize()
                                                                            .combinedClickable(
                                                                                onClick = {
                                                                                    when (item) {
                                                                                        is SongItem -> playerConnection.playQueue(
                                                                                            YouTubeQueue(
                                                                                                item.endpoint ?: WatchEndpoint(videoId = item.id),
                                                                                                item.toMediaMetadata()
                                                                                            )
                                                                                        )
                                                                                        is AlbumItem -> navController.navigate("album/${item.id}")
                                                                                        is ArtistItem -> navController.navigate("artist/${item.id}")

                                                                                        is PlaylistItem -> navController.navigateToPlaylistItem(item)
                                                                                    }
                                                                                },
                                                                                onLongClick = {
                                                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                                                    menuState.show {
                                                                                        when (item) {
                                                                                            is SongItem -> YouTubeSongMenu(
                                                                                                song = item,
                                                                                                navController = navController,
                                                                                                onDismiss = menuState::dismiss
                                                                                            )
                                                                                            is AlbumItem -> YouTubeAlbumMenu(
                                                                                                albumItem = item,
                                                                                                navController = navController,
                                                                                                onDismiss = menuState::dismiss
                                                                                            )
                                                                                            is ArtistItem -> YouTubeArtistMenu(
                                                                                                artist = item,
                                                                                                onDismiss = menuState::dismiss
                                                                                            )
                                                                                            is PlaylistItem -> YouTubePlaylistMenu(
                                                                                                playlist = item,
                                                                                                coroutineScope = scope,
                                                                                                onDismiss = menuState::dismiss
                                                                                            )
                                                                                        }
                                                                                    }
                                                                                }
                                                                            )
                                                                    )
                                                                }
                                                            } else {
                                                                Spacer(modifier = Modifier.width(itemWidth))
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        if (pagerState.pageCount > 1) {
                                            Row(
                                                modifier = Modifier
                                                    .height(24.dp)
                                                    .fillMaxWidth(),
                                                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                repeat(pagerState.pageCount) { iteration ->
                                                    val color = if (pagerState.currentPage == iteration)
                                                        MaterialTheme.colorScheme.primary
                                                    else
                                                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                                    Box(
                                                        modifier = Modifier
                                                            .padding(4.dp)
                                                            .clip(CircleShape)
                                                            .background(color)
                                                            .size(8.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        HomeSection.AiRecommendations -> {
                            aiRecommendedPlaylist?.let { pair ->
                                val (playlist, songs) = pair
                                item(key = "ai_recommendation_title") {
                                    val lastUpdatedStr = playlist.playlist.lastUpdateTime?.let {
                                        "Last updated: " + it.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, h:mm a"))
                                    }
                                    NavigationTitle(
                                        title = playlist.title,
                                        label = lastUpdatedStr,
                                        onClick = {
                                            navController.navigate("local_playlist/${playlist.id}")
                                        },
                                        modifier = Modifier.animateItem()
                                    )
                                }
                                item(key = "ai_recommendation_list") {
                                    LazyRow(
                                        contentPadding = PaddingValues(horizontal = 16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.animateItem()
                                    ) {
                                        items(items = songs, key = { it.id }) { songObj ->
                                            localGridItem(songObj)
                                        }
                                    }
                                }
                            }
                        }
                        HomeSection.QuickPicks -> {
                            quickPicks?.takeIf { it.isNotEmpty() }?.let { quickPicks ->


                                item(key = "quick_picks_list") {
                                    val distinctQuickPicks = quickPicks.distinctBy { it.id }
                                    HorizontalMultiBrowseCarousel(
                                        state = rememberCarouselState { distinctQuickPicks.size },
                                        preferredItemWidth = 250.dp,
                                        itemSpacing = 8.dp,
                                        contentPadding = PaddingValues(horizontal = 16.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(290.dp)
                                            .animateItem()
                                    ) { index ->
                                        val originalSong = distinctQuickPicks[index]
                                        val song by database.song(originalSong.id)
                                            .collectAsState(initial = originalSong)
                                        val isActive = song!!.id == mediaMetadata?.id

                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .maskClip(MaterialTheme.shapes.extraLarge)
                                                .maskBorder(
                                                    BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                                    MaterialTheme.shapes.extraLarge
                                                )
                                                .focusable()
                                                .combinedClickable(
                                                    onClick = {
                                                        if (isActive) {
                                                            playerConnection.togglePlayPause()
                                                        } else {
                                                            playerConnection.playQueue(YouTubeQueue.radio(song!!.toMediaMetadata()))
                                                        }
                                                    },
                                                    onLongClick = {
                                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                        menuState.show {
                                                            SongMenu(
                                                                originalSong = song!!,
                                                                navController = navController,
                                                                onDismiss = menuState::dismiss
                                                            )
                                                        }
                                                    }
                                                )
                                        ) {
                                            AsyncImage(
                                                model = coil3.request.ImageRequest.Builder(LocalContext.current)
                                                    .data(song!!.thumbnailUrl)
                                                    .crossfade(true)
                                                    .build(),
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )

                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .background(
                                                        Brush.verticalGradient(
                                                            colors = listOf(
                                                                Color.Transparent,
                                                                Color.Transparent,
                                                                Color.Black.copy(alpha = 0.7f)
                                                            )
                                                        )
                                                    )
                                            )

                                            if (isActive && isPlaying) {
                                                Box(
                                                    modifier = Modifier
                                                        .align(Alignment.TopEnd)
                                                        .padding(12.dp)
                                                        .size(32.dp)
                                                        .background(
                                                            MaterialTheme.colorScheme.primary,
                                                            CircleShape
                                                        ),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        painter = painterResource(R.drawable.volume_up),
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.onPrimary,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }
                                            }

                                            Column(
                                                modifier = Modifier
                                                    .align(Alignment.BottomStart)
                                                    .padding(16.dp)
                                            ) {
                                                Text(
                                                    text = song!!.title,
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = Color.White,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = song!!.artists.joinToString { it.name },
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = Color.White.copy(alpha = 0.7f),
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        HomeSection.FromTheCommunity -> {
                            communityPlaylists?.takeIf { it.isNotEmpty() }?.let { playlists ->
                                item(key = "community_playlists_title") {
                                    NavigationTitle(
                                        title = stringResource(R.string.from_the_community),
                                        modifier = Modifier.animateItem()
                                    )
                                }

                                item(key = "community_playlists_content") {
                                    LazyRow(
                                        contentPadding = PaddingValues(horizontal = 16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                                        modifier = Modifier.animateItem()
                                    ) {
                                        items(playlists, key = { it.playlist.id }) { item ->
                                            CommunityPlaylistCard(
                                                item = item,
                                                onClick = {
                                                    navController.navigateToPlaylistItem(item.playlist)
                                                },
                                                onSongClick = { song ->
                                                    playerConnection.playQueue(
                                                        YouTubeQueue(
                                                            song.endpoint ?: WatchEndpoint(videoId = song.id),
                                                            song.toMediaMetadata()
                                                        )
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        HomeSection.DailyDiscover -> {
                            dailyDiscover?.takeIf { it.isNotEmpty() }?.let { discoverList ->
                                
                                item(key = "daily_discover_title") {
                                    val title = stringResource(R.string.your_daily_discover)
                                    NavigationTitle(
                                        title = title,
                                        onPlayAllClick = {
                                            val queueItems = discoverList.mapNotNull {
                                                (it.recommendation as? SongItem)?.toMediaMetadata()
                                            }

                                            if (queueItems.isNotEmpty()) {
                                                playerConnection.playQueue(
                                                    ListQueue(
                                                        title = title,
                                                        items = queueItems.map { it.toMediaItem() }
                                                    )
                                                )
                                            }
                                        }
                                    )
                                }
                                item(key = "daily_discover_content") {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(340.dp)
                                            .padding(horizontal = 16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        val carouselState = rememberCarouselState { discoverList.size }
                                        HorizontalMultiBrowseCarousel(
                                            state = carouselState,
                                            preferredItemWidth = 320.dp,
                                            itemSpacing = 16.dp,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(320.dp)
                                        ) { i ->
                                            val item = discoverList[i]
                                            DailyDiscoverCard(
                                                dailyDiscover = item,
                                                onClick = {
                                                    val song = item.recommendation as? SongItem
                                                    val mediaMetadata = song?.toMediaMetadata()
                                                    if (mediaMetadata != null) {
                                                        playerConnection.playQueue(
                                                            YouTubeQueue(
                                                                song.endpoint ?: WatchEndpoint(videoId = song.id),
                                                                mediaMetadata
                                                            )
                                                        )
                                                    }
                                                },
                                                navController = navController,
                                                modifier = Modifier.maskClip(MaterialTheme.shapes.extraLarge)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        HomeSection.KeepListening -> {
                            keepListening?.takeIf { it.isNotEmpty() }?.let { keepListening ->
                                item(key = "keep_listening_title") {
                                    NavigationTitle(
                                        title = stringResource(R.string.keep_listening),
                                        modifier = Modifier.animateItem()
                                    )
                                }

                                item(key = "keep_listening_list") {
                                    val rows = if (keepListening.size > 6) 2 else 1
                                    LazyHorizontalGrid(
                                        state = rememberLazyGridState(),
                                        rows = GridCells.Fixed(rows),
                                        contentPadding = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal)
                                            .asPaddingValues(),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height((currentGridHeight + with(LocalDensity.current) {
                                                MaterialTheme.typography.bodyLarge.lineHeight.toDp() * 2 +
                                                        MaterialTheme.typography.bodyMedium.lineHeight.toDp() * 2
                                            }) * rows)
                                            .animateItem()
                                    ) {
                                        items(keepListening, key = { it.id }) {
                                            localGridItem(it)
                                        }
                                    }
                                }
                            }
                        }
                        HomeSection.AccountPlaylists -> {
                            accountPlaylists?.takeIf { it.isNotEmpty() }?.let { accountPlaylists ->
                                item(key = "account_playlists_title") {
                                    NavigationTitle(
                                        title = "Your Playlists",
                                        onClick = {
                                            navController.navigate("library")
                                        },
                                        modifier = Modifier.animateItem()
                                    )
                                }

                                item(key = "account_playlists_list") {
                                    val distinctPlaylists = accountPlaylists.distinctBy { it.playlist.id }
                                    HorizontalMultiBrowseCarousel(
                                        state = rememberCarouselState { distinctPlaylists.size },
                                        preferredItemWidth = 250.dp,
                                        itemSpacing = 8.dp,
                                        contentPadding = PaddingValues(horizontal = 16.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(290.dp)
                                            .animateItem()
                                    ) { index ->
                                        val playlist = distinctPlaylists[index]
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .maskClip(MaterialTheme.shapes.extraLarge)
                                                .maskBorder(
                                                    BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                                    MaterialTheme.shapes.extraLarge
                                                )
                                                .focusable()
                                                .combinedClickable(
                                                    onClick = {
                                                        navController.navigate("local_playlist/${playlist.playlist.id}")
                                                    },
                                                    onLongClick = {
                                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                        menuState.show {
                                                            iad1tya.echo.music.ui.menu.PlaylistMenu(
                                                                playlist = playlist,
                                                                coroutineScope = scope,
                                                                onDismiss = menuState::dismiss
                                                            )
                                                        }
                                                    }
                                                )
                                        ) {
                                            AsyncImage(
                                                model = coil3.request.ImageRequest.Builder(LocalContext.current)
                                                    .data(playlist.playlist.thumbnailUrl ?: iad1tya.echo.music.R.drawable.ic_widget_play)
                                                    .crossfade(true)
                                                    .build(),
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )

                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .background(
                                                        Brush.verticalGradient(
                                                            colors = listOf(
                                                                Color.Transparent,
                                                                Color.Black.copy(alpha = 0.8f)
                                                            ),
                                                            startY = 100f
                                                        )
                                                    )
                                            )

                                            Column(
                                                modifier = Modifier
                                                    .align(Alignment.BottomStart)
                                                    .padding(16.dp)
                                            ) {
                                                Text(
                                                    text = playlist.playlist.name,
                                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                                    color = Color.White,
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "${playlist.songCount} songs",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = Color.White.copy(alpha = 0.7f),
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        HomeSection.ForgottenFavorites -> {
                            forgottenFavorites?.takeIf { it.isNotEmpty() }?.let { forgottenFavorites ->
                                item(key = "forgotten_favorites_title") {
                                    val forgottenFavoritesTitle = stringResource(R.string.forgotten_favorites)
                                    NavigationTitle(
                                        title = forgottenFavoritesTitle,
                                        modifier = Modifier.animateItem(),
                                        onPlayAllClick = {
                                            playerConnection.playQueue(
                                                ListQueue(
                                                    title = forgottenFavoritesTitle,
                                                    items = forgottenFavorites.distinctBy { it.id }.map { it.toMediaItem() }
                                                )
                                            )
                                        }
                                    )
                                }

                                item(key = "forgotten_favorites_list") {
                                    
                                    val rows = min(4, forgottenFavorites.size)
                                    LazyHorizontalGrid(
                                        state = forgottenFavoritesLazyGridState,
                                        rows = GridCells.Fixed(rows),
                                        contentPadding = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal)
                                            .asPaddingValues(),
                                        flingBehavior = rememberSnapFlingBehavior(
                                            forgottenFavoritesSnapLayoutInfoProvider
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(ListItemHeight * rows)
                                            .animateItem()
                                    ) {
                                        itemsIndexed(
                                            items = forgottenFavorites.distinctBy { it.id },
                                            key = { _, it -> it.id }
                                        ) { index, originalSong ->
                                            val song by database.song(originalSong.id)
                                                .collectAsState(initial = originalSong)

                                            SongListItem(
                                                song = song!!,
                                                showInLibraryIcon = true,
                                                isActive = song!!.id == mediaMetadata?.id,
                                                isPlaying = isPlaying,
                                                isSwipeable = false,
                                                shape = listItemShape(index = index % rows, count = rows),
                                                trailingContent = {
                                                    IconButton(
                                                        onClick = {
                                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                            menuState.show {
                                                                SongMenu(
                                                                    originalSong = song!!,
                                                                    navController = navController,
                                                                    onDismiss = menuState::dismiss
                                                                )
                                                            }
                                                        }
                                                    ) {
                                                        Icon(
                                                            painter = painterResource(R.drawable.more_vert),
                                                            contentDescription = null
                                                        )
                                                    }
                                                },
                                                modifier = Modifier
                                                    .width(horizontalLazyGridItemWidth)
                                                    .combinedClickable(
                                                        onClick = {
                                                            if (song!!.id == mediaMetadata?.id) {
                                                                playerConnection.togglePlayPause()
                                                            } else {
                                                                playerConnection.playQueue(
                                                                    YouTubeQueue.radio(
                                                                        song!!.toMediaMetadata()
                                                                    )
                                                                )
                                                            }
                                                        },
                                                        onLongClick = {
                                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                            menuState.show {
                                                                SongMenu(
                                                                    originalSong = song!!,
                                                                    navController = navController,
                                                                    onDismiss = menuState::dismiss
                                                                )
                                                            }
                                                        }
                                                    )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        is HomeSection.SimilarRecommendation -> {
                            val recommendation = similarRecommendations?.getOrNull(section.index)
                            recommendation?.let {
                                item(key = "similar_to_title_${section.index}") {
                                    NavigationTitle(
                                        label = stringResource(R.string.similar_to),
                                        title = recommendation.title.title,
                                        thumbnail = recommendation.title.thumbnailUrl?.let { thumbnailUrl ->
                                            {
                                                val shape =
                                                    if (recommendation.title is Artist) CircleShape else RoundedCornerShape(
                                                        ThumbnailCornerRadius
                                                    )
                                                AsyncImage(
                                                    model = thumbnailUrl,
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        .size(ListThumbnailSize)
                                                        .clip(shape)
                                                )
                                            }
                                        },
                                        onClick = {
                                            when (recommendation.title) {
                                                is Song -> navController.navigate("album/${recommendation.title.album!!.id}")
                                                is Album -> navController.navigate("album/${recommendation.title.id}")
                                                is Artist -> navController.navigate("artist/${recommendation.title.id}")
                                                is Playlist -> {}
                                            }
                                        },
                                        modifier = Modifier.animateItem()
                                    )
                                }

                                item(key = "similar_to_list_${section.index}") {
                                    LazyRow(
                                        contentPadding = WindowInsets.systemBars
                                            .only(WindowInsetsSides.Horizontal)
                                            .asPaddingValues(),
                                        modifier = Modifier.animateItem()
                                    ) {
                                        items(recommendation.items, key = { it.id }) { item ->
                                            ytGridItem(item)
                                        }
                                    }
                                }
                            }
                        }
                        is HomeSection.HomePageSection -> {
                            val sectionData = homePage?.sections?.getOrNull(section.index)
                            sectionData?.let {
                                
                                val sectionSongs = sectionData.items.filterIsInstance<SongItem>()
                                val hasPlayableSongs = sectionSongs.isNotEmpty()
                                
                                val isSongsOnlySection = sectionData.items.isNotEmpty() &&
                                        sectionData.items.all { it is SongItem }

                                item(key = "home_section_title_${section.index}") {
                                    NavigationTitle(
                                        title = sectionData.title,
                                        label = sectionData.label,
                                        thumbnail = sectionData.thumbnail?.let { thumbnailUrl ->
                                            {
                                                val shape =
                                                    if (sectionData.endpoint?.isArtistEndpoint == true) CircleShape else RoundedCornerShape(
                                                        ThumbnailCornerRadius
                                                    )
                                                AsyncImage(
                                                    model = thumbnailUrl,
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        .size(ListThumbnailSize)
                                                        .clip(shape)
                                                )
                                            }
                                        },
                                        onClick = sectionData.endpoint?.let { endpoint ->
                                            {
                                                when {
                                                    endpoint.browseId == "FEmusic_moods_and_genres" ->
                                                        navController.navigate("mood_and_genres")
                                                    endpoint.params != null ->
                                                        navController.navigate("youtube_browse/${endpoint.browseId}?params=${endpoint.params}")
                                                    else ->
                                                        navController.navigate("browse/${endpoint.browseId}")
                                                }
                                            }
                                        },
                                        onPlayAllClick = if (hasPlayableSongs) {
                                            {
                                                playerConnection.playQueue(
                                                    ListQueue(
                                                        title = sectionData.title,
                                                        items = sectionSongs.map { it.toMediaMetadata().toMediaItem() }
                                                    )
                                                )
                                            }
                                        } else null,
                                        modifier = Modifier.animateItem()
                                    )
                                }

                                if (isSongsOnlySection) {
                                    
                                    item(key = "home_section_list_${section.index}") {
                                        LazyHorizontalGrid(
                                            state = rememberLazyGridState(),
                                            rows = GridCells.Fixed(4),
                                            contentPadding = WindowInsets.systemBars
                                                .only(WindowInsetsSides.Horizontal)
                                                .asPaddingValues(),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(ListItemHeight * 4)
                                                .animateItem()
                                        ) {
                                            itemsIndexed(
                                                items = sectionSongs.distinctBy { it.id },
                                                key = { _, it -> it.id }
                                            ) { index, song ->
                                                YouTubeListItem(
                                                    item = song,
                                                    isActive = song.id == mediaMetadata?.id,
                                                    isPlaying = isPlaying,
                                                    isSwipeable = false,
                                                    shape = listItemShape(index = index % 4, count = 4),
                                                    trailingContent = {
                                                        IconButton(
                                                            onClick = {
                                                                menuState.show {
                                                                    YouTubeSongMenu(
                                                                        song = song,
                                                                        navController = navController,
                                                                        onDismiss = menuState::dismiss
                                                                    )
                                                                }
                                                            }
                                                        ) {
                                                            Icon(
                                                                painter = painterResource(R.drawable.more_vert),
                                                                contentDescription = null
                                                            )
                                                        }
                                                    },
                                                    modifier = Modifier
                                                        .width(horizontalLazyGridItemWidth)
                                                        .combinedClickable(
                                                            onClick = {
                                                                if (song.id == mediaMetadata?.id) {
                                                                    playerConnection.togglePlayPause()
                                                                } else {
                                                                    playerConnection.playQueue(
                                                                        YouTubeQueue.radio(song.toMediaMetadata())
                                                                    )
                                                                }
                                                            },
                                                            onLongClick = {
                                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                                menuState.show {
                                                                    YouTubeSongMenu(
                                                                        song = song,
                                                                        navController = navController,
                                                                        onDismiss = menuState::dismiss
                                                                    )
                                                                }
                                                            }
                                                        )
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    
                                    item(key = "home_section_list_${section.index}") {
                                        LazyRow(
                                            contentPadding = WindowInsets.systemBars
                                                .only(WindowInsetsSides.Horizontal)
                                                .asPaddingValues(),
                                            modifier = Modifier.animateItem()
                                        ) {
                                            items(sectionData.items, key = { it.id }) { item ->
                                                ytGridItem(item)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        HomeSection.MoodAndGenres -> {
                            explorePage?.moodAndGenres?.let { moodAndGenres ->
                                item(key = "mood_and_genres_title") {
                                    NavigationTitle(
                                        title = stringResource(R.string.mood_and_genres),
                                        onClick = {
                                            navController.navigate("mood_and_genres")
                                        },
                                        modifier = Modifier.animateItem()
                                    )
                                }
                                item(key = "mood_and_genres_list") {
                                    LazyHorizontalGrid(
                                        rows = GridCells.Fixed(4),
                                        contentPadding = PaddingValues(6.dp),
                                        modifier = Modifier
                                            .height((MoodAndGenresButtonHeight + 12.dp) * 4 + 12.dp)
                                            .animateItem()
                                    ) {
                                        items(moodAndGenres.distinctBy { it.title }, key = { it.title }) {
                                            MoodAndGenresButton(
                                                title = it.title,
                                                onClick = {
                                                    navController.navigate("youtube_browse/${it.endpoint.browseId}?params=${it.endpoint.params}")
                                                },
                                                modifier = Modifier
                                                    .padding(6.dp)
                                                    .width(180.dp)
                                            )
                                        }
                                    }
                                }
                            }

                        }
                    }
                }

                // ── RECENTLY PLAYED ──────────────────────────────────────────────────
                if (recentlyPlayedSongs.isNotEmpty()) {
                    item(key = "recently_played_header") {
                        HomeSectionHeader(
                            title = "Recently Played",
                            modifier = Modifier.animateItem(),
                        )
                    }
                    item(key = "recently_played_list") {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.animateItem(),
                        ) {
                            items(
                                items = recentlyPlayedSongs,
                                key = { "rp_${it.id}" },
                            ) { song ->
                                CompactMusicCard(
                                    thumbnailUrl = song.thumbnailUrl,
                                    title = song.title,
                                    subtitle = song.artists.joinToString { it.name },
                                    isActive = song.id == mediaMetadata?.id,
                                    isPlaying = isPlaying,
                                    onClick = {
                                        if (song.id == mediaMetadata?.id) {
                                            playerConnection.togglePlayPause()
                                        } else {
                                            playerConnection.playQueue(
                                                YouTubeQueue.radio(song.toMediaMetadata())
                                            )
                                        }
                                    },
                                    onLongClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        menuState.show {
                                            SongMenu(
                                                originalSong = song,
                                                navController = navController,
                                                onDismiss = menuState::dismiss,
                                            )
                                        }
                                    },
                                )
                            }
                        }
                    }
                    item(key = "recently_played_spacer") { Spacer(Modifier.height(8.dp)) }
                }

                // ── MOST PLAYED (COMPACT ROW) ─────────────────────────────────────────
                if (mostPlayedCompact.isNotEmpty()) {
                    item(key = "most_played_compact_header") {
                        HomeSectionHeader(
                            title = "Most Played",
                            modifier = Modifier.animateItem(),
                        )
                    }
                    item(key = "most_played_compact_list") {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.animateItem(),
                        ) {
                            items(
                                items = mostPlayedCompact,
                                key = { "mp_${it.id}" },
                            ) { song ->
                                CompactMusicCard(
                                    thumbnailUrl = song.thumbnailUrl,
                                    title = song.title,
                                    subtitle = song.artists.joinToString { it.name },
                                    isActive = song.id == mediaMetadata?.id,
                                    isPlaying = isPlaying,
                                    onClick = {
                                        if (song.id == mediaMetadata?.id) {
                                            playerConnection.togglePlayPause()
                                        } else {
                                            playerConnection.playQueue(
                                                YouTubeQueue.radio(song.toMediaMetadata())
                                            )
                                        }
                                    },
                                    onLongClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        menuState.show {
                                            SongMenu(
                                                originalSong = song,
                                                navController = navController,
                                                onDismiss = menuState::dismiss,
                                            )
                                        }
                                    },
                                )
                            }
                        }
                    }
                    item(key = "most_played_compact_spacer") { Spacer(Modifier.height(8.dp)) }
                }

                // ── NEW RELEASES ──────────────────────────────────────────────────────
                if (newReleaseAlbums.isNotEmpty()) {
                    item(key = "new_releases_header") {
                        HomeSectionHeader(
                            title = "New Releases",
                            onSeeAll = { navController.navigate("new_release") },
                            modifier = Modifier.animateItem(),
                        )
                    }
                    item(key = "new_releases_list") {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.animateItem(),
                        ) {
                            items(
                                items = newReleaseAlbums,
                                key = { "nr_${it.id}" },
                            ) { album ->
                                CompactAlbumCard(
                                    thumbnailUrl = album.thumbnail,
                                    title = album.title,
                                    subtitle = album.artists?.joinToString(", ") { it.name } ?: "",
                                    onClick = { navController.navigate("album/${album.id}") },
                                    onLongClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        menuState.show {
                                            YouTubeAlbumMenu(
                                                albumItem = album,
                                                navController = navController,
                                                onDismiss = menuState::dismiss,
                                            )
                                        }
                                    },
                                )
                            }
                        }
                    }
                    item(key = "new_releases_spacer") { Spacer(Modifier.height(8.dp)) }
                }

                // ── DISCOVER MORE (Daily Discover as compact row) ─────────────────────
                if (discoverSongs.isNotEmpty()) {
                    item(key = "discover_more_header") {
                        HomeSectionHeader(
                            title = "Discover More",
                            modifier = Modifier.animateItem(),
                        )
                    }
                    item(key = "discover_more_list") {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.animateItem(),
                        ) {
                            items(
                                items = discoverSongs,
                                key = { "dm_${it.id}" },
                            ) { ytItem ->
                                when (ytItem) {
                                    is SongItem -> CompactMusicCard(
                                        thumbnailUrl = ytItem.thumbnail,
                                        title = ytItem.title,
                                        subtitle = ytItem.artists.joinToString(", ") { it.name },
                                        isActive = ytItem.id == mediaMetadata?.id,
                                        isPlaying = isPlaying,
                                        onClick = {
                                            playerConnection.playQueue(
                                                YouTubeQueue(
                                                    ytItem.endpoint ?: WatchEndpoint(videoId = ytItem.id),
                                                    ytItem.toMediaMetadata()
                                                )
                                            )
                                        },
                                        onLongClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            menuState.show {
                                                YouTubeSongMenu(
                                                    song = ytItem,
                                                    navController = navController,
                                                    onDismiss = menuState::dismiss,
                                                )
                                            }
                                        },
                                    )
                                    is AlbumItem -> CompactAlbumCard(
                                        thumbnailUrl = ytItem.thumbnail,
                                        title = ytItem.title,
                                        subtitle = ytItem.artists?.joinToString(", ") { it.name } ?: "",
                                        onClick = { navController.navigate("album/${ytItem.id}") },
                                        onLongClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            menuState.show {
                                                YouTubeAlbumMenu(
                                                    albumItem = ytItem,
                                                    navController = navController,
                                                    onDismiss = menuState::dismiss,
                                                )
                                            }
                                        },
                                    )
                                    else -> {}
                                }
                            }
                        }
                    }
                    item(key = "discover_more_spacer") { Spacer(Modifier.height(8.dp)) }
                }

                if (isLoading || homePage?.continuation != null && homePage?.sections?.isNotEmpty() == true) {
                    item(key = "loading_shimmer") {
                        ShimmerHost(
                            modifier = Modifier.animateItem()
                        ) {
                            // 1. Quick Picks Skeleton
                            Row(
                                modifier = Modifier
                                    .horizontalScroll(rememberScrollState())
                                    .padding(WindowInsets.systemBars.only(WindowInsetsSides.Horizontal).asPaddingValues())
                            ) {
                                repeat(3) {
                                    Spacer(
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp, vertical = 12.dp)
                                            .width(250.dp)
                                            .height(290.dp)
                                            .clip(MaterialTheme.shapes.extraLarge)
                                            .background(MaterialTheme.colorScheme.surfaceVariant)
                                    )
                                }
                            }

                            // 2. Speed Dial Skeleton
                            TextPlaceholder(
                                height = 36.dp,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .width(200.dp),
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp)
                            ) {
                                repeat(2) {
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        repeat(3) {
                                            GridItemPlaceHolder(
                                                modifier = Modifier.weight(1f),
                                                fillMaxWidth = true
                                            )
                                        }
                                    }
                                }
                            }

                            // 3. Generic Row Skeleton
                            TextPlaceholder(
                                height = 36.dp,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .width(250.dp),
                            )
                            Row(
                                modifier = Modifier
                                    .horizontalScroll(rememberScrollState())
                                    .padding(WindowInsets.systemBars.only(WindowInsetsSides.Horizontal).asPaddingValues())
                            ) {
                                repeat(4) {
                                    GridItemPlaceHolder()
                                }
                            }
                        }
                    }
                }

                item(key = "bottom_spacer") {
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }

        }
    }
}

// ─────────────────────────────────────────────────────────────────────────
// LOOPING VIDEO BACKGROUND HELPER
// ─────────────────────────────────────────────────────────────────────────

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun LoopingVideoBackground(videoRes: Int, modifier: Modifier = Modifier) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val exoPlayer = remember {
        androidx.media3.exoplayer.ExoPlayer.Builder(context).build().apply {
            repeatMode = androidx.media3.common.Player.REPEAT_MODE_ONE
            playWhenReady = true
            volume = 0f // Mute background video
        }
    }

    LaunchedEffect(videoRes) {
        val uri = "android.resource://${context.packageName}/$videoRes"
        exoPlayer.setMediaItem(androidx.media3.common.MediaItem.fromUri(uri))
        exoPlayer.prepare()
    }

    androidx.compose.runtime.DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    androidx.compose.ui.viewinterop.AndroidView(
        factory = { ctx ->
            androidx.media3.ui.PlayerView(ctx).apply {
                player = exoPlayer
                useController = false
                resizeMode = androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                // Hide any buffering spinner
                setShowBuffering(androidx.media3.ui.PlayerView.SHOW_BUFFERING_NEVER)
            }
        },
        modifier = modifier
    )
}
