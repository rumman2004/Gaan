# Gaan UI / Playback Backups — 2026-09-28

These files are snapshots taken immediately before the fast-playback and pixel-player update on branch `feat/pixel-history-playback-hardening`.

- `HomeScreen.kt.before-fast-playback-pixel.backup` — current Home screen
- `Player.kt.before-fast-playback-pixel.backup` — current player screen
- `MusicService.kt.before-fast-playback-pixel.backup` — current playback service

The backup files intentionally use a `.backup` suffix so Gradle does not compile them as Kotlin source.

To restore manually, copy the desired backup content over the corresponding source file and commit the restoration.

Additional Home redesign snapshot:

- `HomeScreen.kt.before-anime-greeting-home.backup` — Home screen immediately before the time-aware anime/pixel greeting was added.

The new greeting artwork is rendered with Compose Canvas and is not used as artwork for any song. Song cards/hero continue to load the original song thumbnail URLs.
