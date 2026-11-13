# QVEX Launcher — Android source 0.1.0

This is the first buildable Android project for QVEX Launcher.

## Included
- Minimal dark QVEX dashboard
- AUTO / STABLE FPS / HIGH FPS / MAX POWER profile selector
- Saved profile selection
- Device model and Android version
- System RAM availability
- Android thermal status when supported
- GitHub Actions workflow to build a debug APK

## Important limitations
This is an early UI and device-monitor prototype, **not yet a working Minecraft Java launcher**. The four profiles currently save and display a selection; they do not force CPU/GPU clocks or guarantee FPS gains. Normal Android applications cannot safely force overclocking.

Next milestone: choose and integrate a properly licensed Minecraft Java launcher engine, then implement game settings and real in-game benchmark measurements.

## Build APK with GitHub Actions
1. Extract this ZIP.
2. Upload the contents of the `QVEX-Launcher` folder to the root of your GitHub repository (do not upload the ZIP itself).
3. Commit to branch `main`.
4. Open the repository's Actions tab.
5. Run **Build QVEX Android APK** or wait for the push workflow.
6. Open the completed workflow run and download the `QVEX-Launcher-debug` artifact.

Build configuration: Android Gradle Plugin 8.7.3, Gradle 8.9, JDK 17, compile SDK 35.
