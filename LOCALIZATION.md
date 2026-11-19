# LOCALIZATION.md — Bilingual & RTL Strategy

## Locales
- Default: `ar-EG` (RTL first-class). Fallback: `en` (`values/strings.xml`).
- String parity: one key per line, identical key sets in `values/` and
  `values-ar/`; a parity check fails the build if keys drift.

## Per-app language
- AndroidX per-app language APIs on API 33+; persisted via DataStore.
- Pre-33: resource-locale fallback applied at activity attach.
- Locale stored in `AppSettings`; UI reads it from `SettingsRepository`.

## RTL
- `android:supportsRtl="true"` in manifest and activity.
- Layout uses start/end spacing; icons mirrored where direction matters.
- Mixed Arabic/English names rendered with `BidiFormatter`.

## Formatting (display only)
- Numbers, currency, dates, percentages formatted with the device locale;
  calculations remain exact `BigDecimal` in the domain layer.
- Optional Arabic-Indic digits toggle (default OFF), persisted in settings.

## Neutral storage
- DB stores language-neutral enums; labels resolved through localized mappers.
- Validation messages, warnings, and empty states are bilingual strings, never
  hardcoded in Compose.

## Status
- Base navigation and screen strings present in EN/AR.
- Follow-up: full per-screen string coverage, pluralization, RTL mirroring QA,
  Arabic-Indic digit formatter, TalkBack labels audit.
