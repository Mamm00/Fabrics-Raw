# AGENTS.md — AzharTexCosting agent contract

## Project identity
AzharTexCosting — offline-first Android costing, shrinkage, grade-yield, loss and
pricing-compensation engine for Al Azhar Tex (Cairo, Egypt). Default locale ar-EG
with full RTL; en fallback. Calculations are exact in the pure Kotlin `:domain`
module (BigDecimal, HALF_UP, scale 2); the UI formats for display only.

## Module map
- `:app` — Compose UI, Navigation 3, ViewModels, manual DI wiring, manifest.
- `:domain` — pure Kotlin/JVM: models, rules, calculation engines, result types. **No Android imports.**
- `:data` — Room 3.0 entities/DAOs/migrations, repositories, DataStore settings, benchmark cache.
- `:backend` — Ktor server, `POST /analyze`; API keys live in server env vars only.
- `:benchmark-client` — Ktor Android client, timeouts, offline fallback to cached insight.

## Package naming
Base `com.alazhartex.costing`; `.domain.model`, `.domain.engine`, `.domain.rules`,
`.data.local`, `.data.repo`, `.ui.<screen>`, `.ui.theme`, `.ui.navigation`, `.di`.
PascalCase screens/objects; camelCase classes/functions; `UPPER_SNAKE_CASE` consts;
snake_case only for SQL columns.

## Build & test
- `./gradlew :app:assembleDebug`
- `./gradlew :domain:test :data:testDebugUnitTest :app:testDebugUnitTest`
- `./gradlew :app:connectedDebugAndroidTest` (best-effort in CI without emulator)

## State & UI guardrails (MVI / UDF)
- Collect flows with `collectAsStateWithLifecycle()` — **never** `collectAsState()`.
- Atomic state updates: `_state.update { copy(...) }` — never `_state.value = ...` in a ViewModel.
- No `GlobalScope`; ViewModels use `viewModelScope`.
- No `mutableStateListOf` inside ViewModels; state hoisted to Compose or immutable lists.
- `LazyColumn`/`LazyRow` must set `key` and `contentType`.
- No state mutation during composition; side effects go through LaunchedEffect/DisposableEffect.
- No hardcoded UI strings outside `res/values*`; identical key sets in en and ar.

## Money & math
- Never `Double`/`Float` for money or percentages. Use `BigDecimal` via `Money`
  (HALF_UP, scale 2). Thresholds come from `TextileRules`/Settings — no magic numbers.

## Locale & RTL
- ar-EG default; per-app language API 33+ with DataStore persistence and older-version fallback.
- Start/end spacing, mirrored icons, `BidiFormatter` for mixed AR/EN names.
- Stored enums are language-neutral; labels resolved through localized mappers.

## Branch & commits
- Feature branch `feat/costing-engine`; never push directly to `main`; human merges.
- Conventional Commits: `feat(scope): …`, `fix(scope): …`, `test(scope): …`, `docs(scope): …`.

## Forbidden
TODO/FIXME/XXX, `println(`, `printStackTrace`, non-test `runBlocking`, empty catch,
`AllowMainThreadQueries`, `fallbackToDestructiveMigration`, secrets in repo,
hardcoded strings in Compose, `!!` without a preceding guard.
