# Fix Build Error: jlink executable not found

The build is failing because Gradle's toolchain discovery is picking up a "broken" JDK (actually a JRE) from an IDE extension (`redhat.java`) which is missing the `jlink` executable required by the Android Gradle Plugin.

## Proposed Changes

We will migrate the project to use JDK 24, which is available on the system and contains a valid `jlink` executable.

### Build Configuration

#### [MODIFY] [gradle.properties](file:///D:/Rumman Ahmed/New folder/gradle.properties)
Update `org.gradle.java.home` and `org.gradle.java.installations.paths` to point to the valid JDK 24 installation.

#### [MODIFY] [gradle-daemon-jvm.properties](file:///D:/Rumman Ahmed/New folder/gradle/gradle-daemon-jvm.properties)
Update `toolchainVersion` to `24` to ensure the Gradle daemon runs on a valid JDK.

#### [MODIFY] [Subproject build.gradle.kts files]
Update `jvmToolchain(21)` to `jvmToolchain(24)` in the following files:
- [applecanvas/build.gradle.kts](file:///D:/Rumman Ahmed/New folder/applecanvas/build.gradle.kts)
- [artistvideo/build.gradle.kts](file:///D:/Rumman Ahmed/New folder/artistvideo/build.gradle.kts)
- [betterlyrics/build.gradle.kts](file:///D:/Rumman Ahmed/New folder/betterlyrics/build.gradle.kts)
- [canvas/build.gradle.kts](file:///D:/Rumman Ahmed/New folder/canvas/build.gradle.kts)
- [echomusiccanvas/build.gradle.kts](file:///D:/Rumman Ahmed/New folder/echomusiccanvas/build.gradle.kts)
- [innertube/build.gradle.kts](file:///D:/Rumman Ahmed/New folder/innertube/build.gradle.kts)
- [kugou/build.gradle.kts](file:///D:/Rumman Ahmed/New folder/kugou/build.gradle.kts)
- [lrclib/build.gradle.kts](file:///D:/Rumman Ahmed/New folder/lrclib/build.gradle.kts)
- [paxsenixlyrics/build.gradle.kts](file:///D:/Rumman Ahmed/New folder/paxsenixlyrics/build.gradle.kts)
- [shazamkit/build.gradle.kts](file:///D:/Rumman Ahmed/New folder/shazamkit/build.gradle.kts)
- [simpmusic/build.gradle.kts](file:///D:/Rumman Ahmed/New folder/simpmusic/build.gradle.kts)
- [unison/build.gradle.kts](file:///D:/Rumman Ahmed/New folder/unison/build.gradle.kts)
- [youlyplus/build.gradle.kts](file:///D:/Rumman Ahmed/New folder/youlyplus/build.gradle.kts)

## Verification Plan

### Automated Tests
- Run `./gradlew :app:assembleDebug` to verify that the project builds successfully.
