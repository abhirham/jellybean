# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Jellyfin Android TV is a Kotlin-based Android TV client for Jellyfin media servers. The app targets Android TV, Nvidia Shield, and Amazon Fire TV devices, with minimum SDK 21 and target SDK 36.

## Build Commands

### Build APKs
```bash
./gradlew assembleDebug     # Build debug APK
./gradlew assembleRelease   # Build release APK (optimized)
```

### Install to Device/Emulator
```bash
./gradlew installDebug      # Install debug build
./gradlew installRelease    # Install release build
```

### Code Quality
```bash
./gradlew detekt            # Run Detekt static analysis
./gradlew lint              # Run Android lint
```

### Testing
```bash
./gradlew test              # Run all unit tests
./gradlew testDebug         # Run debug unit tests
```

Tests are located in `app/src/test/kotlin/` and use Kotest framework with JUnit 5.

## Module Structure

The project is organized into several Gradle modules:

- **app**: Main application module containing UI, activities, fragments, and app-specific logic
- **playback:core**: Core playback abstraction layer
- **playback:jellyfin**: Jellyfin-specific playback implementation
- **playback:media3:exoplayer**: ExoPlayer integration for media playback
- **playback:media3:session**: Media3 session handling
- **preference**: Shared preference management

## Architecture

### Dependency Injection
The app uses **Koin** for dependency injection. Module definitions are in `app/src/main/java/org/jellyfin/androidtv/di/`:
- `AppModule.kt` - Core app dependencies
- `PlaybackModule.kt` - Playback-related dependencies
- `AuthModule.kt` - Authentication dependencies
- `AndroidModule.kt` - Android framework components
- `UtilsModule.kt` - Utility classes
- `PreferenceModule.kt` - Preference management

### UI Layer
UI components are organized by feature in `app/src/main/java/org/jellyfin/androidtv/ui/`:
- `playback/` - Video/audio playback UI and controls
- `home/` - Home screen and navigation
- `browsing/` - Content browsing views
- `itemdetail/` - Item detail screens
- `search/` - Search functionality
- `livetv/` - Live TV guide and playback
- `composable/` - Jetpack Compose UI components
- `startup/` - App startup and onboarding flows
- `preference/` - Settings screens

Mix of traditional Android Views (Java/Kotlin) and newer Jetpack Compose components.

### Data Layer
Data management in `app/src/main/java/org/jellyfin/androidtv/data/`:
- `repository/` - Data repositories
- `service/` - API services
- `model/` - Data models
- `querying/` - Data query builders

### Playback Architecture
Playback is modular and abstracted:
- Core playback interfaces in `playback:core`
- Jellyfin-specific logic in `playback:jellyfin`
- ExoPlayer implementation in `playback:media3:exoplayer`
- Media session handling in `playback:media3:session`

App UI layer interacts with playback through `MediaManager` and related controllers in `app/src/main/java/org/jellyfin/androidtv/ui/playback/`.

## Key Dependencies

- **Jellyfin SDK**: `org.jellyfin.sdk:jellyfin-core` - Core Jellyfin API client
- **Kotlin Coroutines**: Async programming
- **Jetpack Compose**: Modern declarative UI (alongside legacy Views)
- **AndroidX Leanback**: TV-optimized UI components
- **Media3/ExoPlayer**: Media playback
- **Koin**: Dependency injection
- **Coil**: Image loading
- **Timber**: Logging
- **ACRA**: Crash reporting
- **Kotest + MockK**: Testing framework

SDK versions can be overridden with gradle properties:
- `sdk.version=local` - Use `latest-SNAPSHOT` from mavenLocal
- `sdk.version=snapshot` - Use `master-SNAPSHOT`
- `sdk.version=unstable-snapshot` - Use `openapi-unstable-SNAPSHOT`

## Build Configuration

### Version Management
Version names are set via environment variable `JELLYFIN_VERSION` or gradle property `jellyfin.version`, falling back to `0.0.0-dev.1`. Version codes are automatically calculated from semantic versions (see `buildSrc/src/main/kotlin/VersionUtils.kt`).

### Build Variants
- **Debug**: Uses application ID suffix `.debug` to allow side-by-side installation with release builds
- **Release**: Production build without minification

### Code Quality Tools
- **Detekt**: Kotlin static analysis with config in `detekt.yaml`
- **Android Lint**: XML configuration in `android-lint.xml`
- **EditorConfig**: Code formatting standards in `.editorconfig`

Configuration highlights from `detekt.yaml`:
- Function naming ignores `@Composable` annotations (PascalCase allowed)
- Increased thresholds for `TooManyFunctions` (15 per class/interface)
- TODO/FIXME comments allowed, only STOPSHIP forbidden
- Max return count: 6

## Development Notes

- **Java 21** toolchain required (configured in `build.gradle.kts`)
- **Kotlin 2.2.21** with JVM target 1.8
- Core library desugaring enabled for modern Java APIs on older Android versions
- Mix of Java and Kotlin code (legacy Java views being migrated to Kotlin)
- ViewBinding and BuildConfig features enabled
- Compose compiler plugin configured for Jetpack Compose support
