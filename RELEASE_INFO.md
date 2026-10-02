# Gaan Release Notes

## Version 5.8.96

### 🚀 What's New & Improved
- **Spotify Playlist Sync:** Added a new "Sync" button for imported Spotify playlists, allowing you to instantly fetch and merge newly added songs directly into the app!
- **Adaptive App Icon:** Designed a proper Adaptive Icon for Android 8+ devices so the app icon displays beautifully without a forced white background circle.
- **About Page Logo:** Restored the original glowing colors of the transparent Gaan logo in the About screen.

### 🛠️ Bug Fixes & Stability
- Fixed GitHub Actions CI pipeline issues with KSP crashes and Universal APK uploading.
- Resolved Resource "Multiple substitutions" errors caused by duplicate launcher icons.

---

## Version 5.8.95

### 🚀 What's New & Improved
- **Automated App Icons:** Completely rebuilt the Android app icon build system to automatically crop and generate high-quality launcher icons at build-time.
- **Streaming Reliability:** Synchronized internal player configurations with upstream to fix recent music streaming API breakages.

### 🛠️ Bug Fixes & Stability
- **Gradle Pipeline Stability:** Fixed a severe memory leak and crashing issue with KSP (Kotlin Symbol Processing) during the CI build process.
- **Build Toolchain Updates:** Addressed multiple APK packaging path issues and implicit dependency conflicts to ensure compatibility with Android Gradle Plugin 9.0.

---

## Version 5.8.94

### 🚀 What's New & Improved
- **Support Screen:** Introduced a new Support screen where users can view the app icon and easily make UPI donations via a static QR code.
- **Listen Together Sync:** Added robust state synchronization features to keep group listening sessions perfectly timed!
- **Your Playlists:** Added a dedicated "Your Playlists" hub for easier local playlist management.
- **Spotify Sync Engine:** Laid the architectural groundwork and database foundations for syncing updated playlists from Spotify.

### 🛠️ Bug Fixes & Stability
- Fixed several UI text visibility issues and corrected misaligned icon resources across the app.

---

## Version 5.2.93

### 🚀 What's New & Improved
- **Anime Pixel Home:** Added a dynamic anime-inspired pixel-art home greeting with time-aware greeting, clock, and date.
- **Playback & Preload:** Improved playback stream resolution caching and upcoming-track preloading.
- **Pixel Player UI:** Added lightweight pixel-art visual overlays while keeping original song artwork intact.

### 🛠️ Bug Fixes & Stability
- Improved Home screen visual consistency and playback responsiveness.

---

## Version 5.2.91

### 🚀 What's New & Improved
- **Smart Beta Updates**: Beta testers on the Nightly channel will now receive instant in-app update notifications whenever new code is pushed, without requiring manual app version updates!

### 🛠️ Bug Fixes & Stability
- **Playback Error Fixed**: Fixed the critical `IO_UNSPECIFIED (2000)` / `403` error when playing YouTube tracks by prioritizing the iOS client, bypassing recent restrictions.
- **Hero Carousel Fix**: Resolved an issue where the Home Screen hero carousel was completely invisible on certain Android devices and screen sizes.

---

## Version 5.2.90

### 🚀 What's New & Improved
- **Voice Assistant Integration**: Gaan is now fully controllable via Google Assistant and Gemini! Say "Play [Song] on Gaan" and it will instantly search and auto-play your favorite tracks.
- **Modernized Settings Menu**: Upgraded the settings profile window to a sleek, modern bottom sheet for a more intuitive user experience.
- **Enhanced Search**: Improved searchable text within settings, making it much easier to find options.
- **Community Links**: Added direct links to the official Gaan Telegram community channel in the Welcome dialog and About screen.
- **Automated Releases**: Cleaned up the release workflow and tailored the Telegram bot to deliver pristine release notes to the community.
- **Streamlined UI**: Removed unused upstream screens and settings (like lossless contributions) for a cleaner, Gaan-focused experience.

### 🛠️ Bug Fixes & Stability
- **Lyrics Rendering Fix**: Resolved a critical issue where Hindi and Punjabi lyrics would break apart, ensuring perfect text rendering.
- **Lyrics Blur Crash Fix**: Patched a crash related to lyrics blur affecting devices on Android 12+ (API 31).
- **Casting Improvements**: Fixed bugs with the volume slider and queue looping while casting to TVs and Smart Speakers.

---

## Version 5.2.89

### 🎧 Listen Together Enhancements
- Added a new "Force Sync" button to manually synchronize playback state
- Fixed a bug causing the player to get permanently out of sync when songs buffered slowly
- Fixed an issue where the player could get stuck on a previous song during rapid track changes
- Added an in-app Toast notification for incoming Join Requests so hosts never miss them

### ✨ General Enhancements
- Fixed Update Notification parsing to properly alert users of new versions
- Improved notification delivery with proper formatting
- Integrated Telegram Bot and Discord Webhook for automated release announcements
- Fixed Spotify login integration so that accounts connect and sync successfully
- Ensured Spotify playlist import functionality works properly
- Redesigned the Home Screen UI to feature a new Hero Carousel for User Playlists