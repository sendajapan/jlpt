# Android Client — Onboarding

Native Android client for the JLPT/PathLingo platform. Consumes the Laravel mobile REST API at `/api/v1/*`.

## Stack

- Kotlin + Jetpack Compose (auth flow) and legacy Java fragments (catalog/lessons UI)
- Networking: Retrofit 2.11 + OkHttp 4.12 + Moshi 1.15
- DI: none — hand-rolled `ServiceLocator` singleton at `app/src/main/java/com/scholarlyapps/pathlingo/data/remote/ServiceLocator.kt`
- Token storage: DataStore Preferences (`data/local/TokenStore.kt`)
- Google sign-in: Credential Manager + `googleid` library; ID token validated server-side by `POST /auth/social/google`

## Boot flow

1. Launcher = `ui.auth.AuthActivity` (Compose). It calls `ServiceLocator.init(this)`.
2. `SplashScreen` checks the stored Sanctum token. If `/me` succeeds, jumps straight to `MainDashboardActivity`. Otherwise routes to `LoginScreen`.
3. `LoginScreen` / `SignupScreen` → `AuthViewModel` → `AuthRepository` → API.
4. On success, `DataManager.loadData(applicationContext)` warms catalog + user caches and `MainDashboardActivity` starts.

`DataManager` (`data/DataManager.kt`) is a singleton that fetches catalog + me in parallel and maps API DTOs into the legacy Java POJOs (`models/Category`, `Subcategory`, `Word`, `User`) so existing Java fragments continue to work without changes.

## Configuration

`app/build.gradle.kts` exposes two `BuildConfig` fields:

- `API_BASE_URL` — currently `https://pathlingo.scholarlyapps.com/api/v1/`
- `GOOGLE_WEB_CLIENT_ID` — read from `gradle.properties` (`GOOGLE_WEB_CLIENT_ID=...`)

Set the Google ID in **two** places when you have it:

1. `android/gradle.properties` → `GOOGLE_WEB_CLIENT_ID=<id>`
2. Laravel `.env` → `GOOGLE_CLIENT_IDS=<id>` (comma-separated; backend validates against this list in `config/services.php`)

## Building

System prerequisites: JDK 21, Android SDK with `platforms;android-36` + `build-tools;36.0.0`. The wrapper jar is gitignored; regenerate with `gradle wrapper` if missing.

```
cd android
./gradlew :app:assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk`.

## Conventions

- **Read-only catalog**: the mobile app must not expose any create/edit/delete UI for categories, subcategories, or vocabularies. Admin writes happen only in the Laravel admin.
- All new screens should be Compose. Legacy Java fragments stay until refactored individually.
- Use `ServiceLocator.authRepository`, `catalogRepository`, `userRepository` rather than constructing Retrofit clients ad hoc.
- Bridging Kotlin → Java: expose `@JvmStatic` helpers (see `ui/auth/LogoutHelper.kt`) instead of forcing fragments to deal with `suspend` functions.

## Endpoints in use

Defined in `data/remote/ApiService.kt`:

- Public: `GET categories`, `GET subcategories`, `GET vocabularies`
- Auth: `POST auth/register`, `POST auth/login`, `POST auth/social/google`, `POST auth/forgot-password`, `POST auth/logout`
- Authenticated: `GET me`, `GET favorites`, `POST favorites/{id}`, `DELETE favorites/{id}`, `GET me/reads`, `POST me/reads/{id}`, `GET me/coins/balance`, `GET me/coins/transactions`

## Open follow-ups

- Facebook login (backend route exists at `POST /auth/social/facebook`; no UI yet)
- Offline cache layer beyond in-memory `DataManager`
- Instrumented tests (none added in the initial wire-up)
