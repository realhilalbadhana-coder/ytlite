# YTLite

A small, clean Android app (single WebView) with a dark, YouTube-style
interface: header + search, category chips, a horizontal "Short videos"
row, a recommended feed, and a full-screen player.

## How it works (and what it is *not*)

- The whole UI lives in `app/src/main/assets/index.html`.
- Playback uses YouTube's **official embed player** (`youtube.com/embed/...`).
- Video titles/authors come from YouTube's public **oEmbed** endpoint, so the
  app needs no API key to look alive.
- Optional: paste a YouTube Data API v3 key into `API_KEY` in `index.html`
  to enable real search.

This project intentionally does **not** scrape or re-host YouTube streams,
strip ads, or reproduce YouTube Premium. It is a normal, ad-supported
YouTube client shell that stays inside YouTube's terms of service.

## Build the APK without a PC (free, via GitHub)

1. Create a new **empty** repository on GitHub (no README, no .gitignore).
2. Upload this whole project folder to it (GitHub's web uploader is fine).
3. Go to the **Actions** tab. The workflow **Build Android APK** runs on its
   own; if not, click it and press **Run workflow**.
4. When it finishes (green tick, ~2-3 min), open the run and download the
   artifact **ytlite-debug-apk**. Unzip it to get `app-debug.apk`.
5. Copy that `.apk` to your phone, tap it, and allow "Install unknown apps"
   for your file manager. Done.

## Build locally (if you have Android Studio)

Open the folder in Android Studio and press Run, or:
`gradle assembleDebug` -> `app/build/outputs/apk/debug/app-debug.apk`.

## Requirements

- JDK 17, Android SDK (compileSdk 34), Gradle 8.7.
- minSdk 24 (Android 7.0+), portrait, single activity.
