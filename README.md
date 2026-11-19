# AzharTexCosting — Al Azhar Tex Costing & Grade Compensation (Android)

Production-grade Android costing engine for a textile wholesale house: raw-material
intake, shrinkage, grade yield & loss, layered cost build-up, pricing/compensation
solving, sensitivity analysis, and benchmark insights.

## Stack (2026)
- Kotlin 2.2+, Jetpack Compose, Material 3 Expressive, Navigation 3, WindowSizeClass adaptive layouts, dark/light theme.
- MVVM + Clean Architecture: `:app` / `:domain` / `:data` / `:backend` / `:benchmark-client`.
- Room 3.0, Flow DAOs, schema export to `schemas/`, explicit migrations.
- Pure Kotlin `:domain` engine with `BigDecimal` (HALF_UP, scale 2) — no Double for money.
- Ktor backend `POST /analyze`; keys stay server-side; Android client degrades offline.
- Full Arabic/English, RTL-first, per-app locale, locale-aware display formatting.

## Modules
| Module | Purpose |
|---|---|
| `:app` | Compose screens, navigation, ViewModels, DI |
| `:domain` | Calculation engines, models, rules, unit tests |
| `:data` | Room entities/DAOs/migrations, repositories, settings, cache |
| `:backend` | Ktor benchmark endpoint |
| `:benchmark-client` | Android Ktor client + offline fallback |

## Screens
Dashboard · Batch List · Batch Intake/Edit · Shrinkage · Grade Yield & Loss ·
Cost Breakdown · Pricing & Compensation · Benchmark Insight · Settings/Language.

## Build
```bash
./gradlew :app:assembleDebug
./gradlew :domain:test :app:testDebugUnitTest
```

## Docs
- [`AGENTS.md`](AGENTS.md) — agent contract, state/UI guardrails, forbidden patterns.
- `CALCULATIONS.md` — formulas + AR/EN textile glossary (to be completed).
- `RESEARCH.md` — benchmark sources (to be completed).
- `LOCALIZATION.md` — locale/RTL strategy (to be completed).

## Status
Scaffold + domain engine in progress on branch `feat/costing-engine`.
