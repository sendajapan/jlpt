---
name: project-overview
description: "High-level layout of jlpt-laravel-vue: Laravel 13 admin/API + Android client + sibling android-push project"
metadata: 
  node_type: memory
  type: project
  originSessionId: 0566fa33-6893-47f9-a85f-72dee5d4ea77
---

Repo `/var/www/html/jlpt-laravel-vue` contains:

- Laravel 13 admin (Blade + Vue islands) with Sanctum-protected mobile API exposed under `/api/v1/*` (see `routes/api.php`). API resources, controllers, and `SocialLoginService` already documented via l5-swagger.
- `android/` — native Android client (Kotlin + Jetpack Compose for auth, Java fragments for legacy screens). Consumes the `/api/v1/*` API. See [[project-android-integration]].
- `android/` (separate android push project lives under a sibling folder per recent commits) — push notification scaffold, unrelated to the main client app.

Production API base URL: `https://pathlingo.scholarlyapps.com/api/v1/`.

**Why:** Knowing the two-app split prevents confusion when the user says "android" — they mean the main client unless they say "push".
**How to apply:** When asked about mobile features, default to the main Android client. Confirm before touching `android-push`.
