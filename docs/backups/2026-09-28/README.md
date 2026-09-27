# Gaan UI / Playback Backups — 2026-09-28

These files are snapshots taken immediately before the fast-playback and pixel-player update on branch `feat/pixel-history-playback-hardening`.

- `HomeScreen.kt.before-fast-playback-pixel.backup` — current Home screen
- `Player.kt.before-fast-playback-pixel.backup` — current player screen
- `MusicService.kt.before-fast-playback-pixel.backup` — current playback service

The backup files intentionally use a `.backup` suffix so Gradle does not compile them as Kotlin source.

To restore manually, copy the desired backup content over the corresponding source file and commit the restoration.