---
name: project-android-integration
description: Android client wired to Laravel API with Compose auth (commit ca92149) — pending Google Web Client ID
metadata: 
  node_type: memory
  type: project
  originSessionId: 0566fa33-6893-47f9-a85f-72dee5d4ea77
---

Commit `ca92149` on `main` (2026-05-21) wired the Android app at `android/` to the live Laravel API and rewrote auth in Jetpack Compose.

Key architecture:
- Networking: Retrofit + OkHttp + Moshi, no DI; hand-rolled `ServiceLocator` singleton at `android/app/src/main/java/com/scholarlyapps/pathlingo/data/remote/ServiceLocator.kt`.
- Token: DataStore Preferences via `data/local/TokenStore.kt`; `AuthInterceptor` injects `Authorization: Bearer <token>` except on auth endpoints.
- Auth UI: `ui/auth/AuthActivity.kt` is the launcher; Compose `NavHost` with splash/login/signup. Google sign-in via Credential Manager + `googleid`; ID token validated server-side by `POST /auth/social/google`.
- `data/DataManager.kt` (Kotlin) replaced the old Java DataManager; still maps DTOs into legacy POJOs (`models/Category`, `Subcategory`, `Word`, `User`) so existing Java fragments work unchanged.
- Mobile is **read-only** for category/subcategory/vocabulary — no create/edit screens.

Pending from user:
- Google Web OAuth Client ID. When provided: set `GOOGLE_WEB_CLIENT_ID=...` in `android/gradle.properties` AND add the same ID to Laravel `.env` `GOOGLE_CLIENT_IDS=` (comma-separated). Rebuild Android.
- Facebook login UI not implemented (backend endpoint exists at `POST /auth/social/facebook`).

BuildConfig fields (in `android/app/build.gradle.kts`): `API_BASE_URL`, `GOOGLE_WEB_CLIENT_ID`.

Build environment notes (for future Claude on the same VM):
- Android SDK at `/root/android-sdk`, JDK 21 (`openjdk-21-jdk-headless`). Gradle wrapper jar is gitignored — regenerate with `gradle wrapper` from `/tmp/gradle-8.13` if missing.
- `cd android && ./gradlew :app:assembleDebug` produces `app/build/outputs/apk/debug/app-debug.apk`.

**Why:** This was a multi-day rewrite — keeping the exact file paths and pending items prevents redoing investigation.
**How to apply:** When user mentions Android auth, networking, or DataManager, start from these files. When they finally supply the Google client ID, just slot it into the two places above.
