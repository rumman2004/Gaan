# Gaan Release Notes

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