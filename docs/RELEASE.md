# Release status

v1.1.0 (UI/UX v2) is published automatically by `.github/workflows/release.yml`
whenever `feat/costing-engine` or `main` is pushed: the workflow builds the debug
APK and creates/updates the `v1.1.0` release with it attached.

Locally signed build:

```
gradle :app:assembleRelease
apksigner verify --print-certs app/build/outputs/apk/release/app-release.apk
```

Triggered at 2026-11-19T07:15Z after repository Actions settings were updated.
