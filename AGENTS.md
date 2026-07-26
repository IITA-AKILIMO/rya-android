# AGENTS.md — RYA Android

## Project

Single-module Android app for rapid cassava yield assessment (AKILIMO RYA).
Package: `com.akilimo.rya`. MinSDK 21, TargetSDK 35.

## Key Commands

```bash
# Build
./gradlew assembleDebug
./gradlew assembleRelease

# Tests (CI runs testRelease specifically)
./gradlew testRelease

# Single test class
./gradlew testRelease --tests "com.akilimo.rya.utils.FieldComputationsTest"

# Lint / static analysis
./gradlew detekt

# Full CI-equivalent check
./gradlew testRelease detekt
```

## Toolchain

| Tool | Version |
|------|---------|
| Gradle | 8.13 |
| AGP | 8.13.2 |
| Kotlin | 2.0.0 |
| JDK | 21 |
| compileSdk | 35 |
| minSdk | 21 |

Both **kapt** (Room compiler) and **KSP** plugins are applied. Room uses kapt.

## Architecture

- Single `:app` module only (no multi-module)
- Room database (`AppDatabase.kt`) with destructive migration, DB name includes date stamp
- ViewBinding enabled, no Compose
- Navigation component for screen flow
- Stepper UI pattern (`MaterialStepper` library)
- Views split into `activities/` and `fragments/`

## Versioning

Version is computed dynamically in `app/build.gradle`:
- `RELEASE_VERSION` env var takes precedence (from CI tag fetcher)
- Fallback: date-based `major.month.day` format
- `nextrelease.txt` is written on `main` branch builds with the computed tag
- `beta` branch appends `-beta-BUILD_NUMBER`

## Branching & CI

- `develop` → auto-creates PR to `main`
- `beta` → builds AAB, signs, publishes to Play Store beta track
- `main` → builds AAB, signs, publishes to Play Store production, creates GitHub release + tag
- CI runs `./gradlew testRelease` (with `continue-on-error: true`)

## Code Quality

- **Detekt**: config at `config/detekt/detekt.yml`, baseline at `app/detekt-baseline.xml`
  - Max line length: 120
  - `warningsAsErrors: false`, `maxIssues: 0`
  - ForbiddenComment rule blocks `TODO:`, `FIXME:`, `STOPSHIP:` in code
- **SonarQube**: configured in `app/build.gradle`, project key `IITA-AKILIMO_rya-android_AYWat7AVA_a0kZ5UNFXL`

## Commit Conventions

Enforced by pre-commit hooks:
- **commitizen** (commit-msg stage) — conventional commits format
- **gitlint** — config in `.gitlint`, ignores `title-trailing-punctuation`, rules T3, B6
- Pre-commit also runs: `check-yaml`, `end-of-file-fixer`, `trailing-whitespace`

## Gotchas

- Room DB uses `allowMainThreadQueries()` — ok for this app but don't introduce blocking calls in new background-sensitive code
- `repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)` — all repos must be in `settings.gradle`, not per-module `build.gradle`
- JitPack is a dependency source (`https://jitpack.io`) — some deps come from GitHub repos directly
- Debug build type injects `PORT_NUMBER` string resource with value `9085`
- `android.enableJetifier=true` is on in `gradle.properties`
- `kotlin.incremental=false` — incremental compilation is disabled
