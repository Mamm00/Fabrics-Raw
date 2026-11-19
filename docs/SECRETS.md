# Secrets & Keys — Handling Rules

**Short answer: no secret key is committed to GitHub, and none belongs in the
Android app or in GitHub Pages static files.** The benchmark web-search API key
lives only on the backend server.

## Where keys live

| Key | Where stored | Reachable by Android? |
|---|---|---|
| Benchmark / web-search provider API key | Backend server environment variable (Ktor `:backend`) | **No** — Android calls our own backend, never the provider directly |
| Backend base URL | Android `local.properties` / BuildConfig or a remote config; not a secret itself | Yes (public endpoint URL) |
| Any future DB / auth secret | Server env vars or GitHub Actions secrets | No |

## How each environment supplies keys

### Local development
- Backend: copy `backend/.env.example` to `backend/.env` (git-ignored), fill in
  `BENCHMARK_API_KEY`. Loaded from process environment / dotenv at startup.
- Android: non-secret endpoints go in `local.properties` (git-ignored), read into
  BuildConfig at build time.

### CI / GitHub Actions
- Store provider keys as **repository Secrets** (Settings → Secrets and variables
  → Actions), e.g. `BENCHMARK_API_KEY`.
- Workflow injects them as env vars at job time; they are never written to the
  repo or printed in logs.

### Production backend
- Deploy the Ktor backend to a host (VPS / Render / Fly / etc.) and set the
  provider key in the host's environment variables. The Android app only knows
  the public backend URL.

## GitHub Pages note
This project is an **Android app + Ktor backend**, not a GitHub Pages site.
Even so: GitHub Pages serves static files publicly, so **never** place an API
key, token, or password in any HTML/JS/JSON that gets published there. Anything
shipped to the client is visible to every visitor.

## Forbidden
- Hardcoding keys in source, `gradle.properties`, `strings.xml`, JSON assets, or docs.
- Committing `.env`, `local.properties`, `secrets.properties`.
- Logging or printing secrets; embedding provider keys in the APK.
- Reusing a leaked key — rotate it at the provider, then update the secret store.

## Rotation checklist if a key leaks
1. Revoke/regenerate the key in the provider dashboard.
2. Update the backend env var / CI secret.
3. Search git history for the old value and purge it if it was ever committed.
