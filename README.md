# Android Basic Skeleton

[![Codecov](https://codecov.io/gh/santimattius/android-basic-skeleton/branch/master/graph/badge.svg?token=HNW9TXKMQU)](https://codecov.io/gh/santimattius/android-basic-skeleton)
[![Quality Checks](https://github.com/santimattius/android-basic-skeleton/actions/workflows/main.yml/badge.svg)](https://github.com/santimattius/android-basic-skeleton/actions)
[![AGP 9.4.0](https://img.shields.io/badge/AGP-9.4.0-blue.svg)](https://developer.android.com/build/releases/gradle-plugin)
[![Kotlin 2.4.20](https://img.shields.io/badge/Kotlin-2.4.20-purple.svg)](https://kotlinlang.org/docs/whatsnew20.html)

A production-ready Android skeleton project featuring modern architecture, essential build configurations, and automated quality checks. Designed to accelerate the development of high-quality Android applications.

## 🚀 Key Features

- **Modern Tech Stack**: Jetpack Compose, Hilt, Coroutines, and Flow.
- **Robust Networking**: Retrofit with Gson and OkHttp integration.
- **Built-in Quality Control**: Integrated Detekt for static analysis and Jacoco for code coverage.
- **AGP 9.0+ Ready**: Configured with the latest Android Gradle Plugin defaults, including built-in Kotlin support and New DSL interfaces.
- **CI/CD Integrated**: Pre-configured GitHub Actions for automated testing and secret management.

## 🏗 Architecture & Design

The project follows modern Android development patterns:
- **Dependency Injection**: Hilt for compile-time safe DI.
- **UI Framework**: 100% Jetpack Compose for a reactive UI.
- **Asynchronous Programming**: Kotlin Coroutines and Flow for seamless data handling.
- **Image Loading**: Coil for efficient image loading in Compose.

## 🛠 Project Structure

```text
├── app/                  # Main application module
│   ├── src/main/java/    # Source code (Hilt DI, UI Components, ViewModels)
│   ├── src/test/         # Unit + Robolectric tests
│   ├── src/androidTest/  # Instrumented tests (Hilt, device-only)
│   ├── src/screenshotTest/ # Compose Preview Screenshot tests
│   └── build.gradle.kts  # App-specific build configuration (incl. Jacoco)
├── config/               # Configuration files (Detekt, etc.)
├── docs/
│   └── testing.md        # Testing strategy: analysis, plan, and what's implemented
├── gradle/               # Gradle scripts and version catalog
│   └── libs.versions.toml # Centralized dependency management
├── AGENTS.md             # Entry point for agent-facing project docs
└── plugins/              # Custom build plugins
```

## 🚦 Getting Started

### Prerequisites
- Android Studio Ladybug | 2024.2.1 or newer
- JDK 17 (configured in Gradle settings)

### Verification & Testing

Run project-wide quality checks:
```bash
./gradlew check
```

Execute unit tests (includes Robolectric-based Compose behavior tests):
```bash
./gradlew :app:testDebugUnitTest
```

Execute instrumented tests (requires a connected device or emulator):
```bash
./gradlew :app:connectedDebugAndroidTest
```

Run static analysis (Detekt):
```bash
./gradlew :app:detekt
```

### Screenshot Testing

Update reference images after an intentional UI change:
```bash
./gradlew :app:updateDebugScreenshotTest
```

Validate the current UI against the committed reference images:
```bash
./gradlew :app:validateDebugScreenshotTest
```
*Reference images live at: `app/src/screenshotTestDebug/reference/`*

### Code Coverage Reports

Generate a Jacoco coverage report from unit tests:
```bash
./gradlew :app:jacocoTestReport
```
*Report is generated at: `app/build/reports/jacoco/jacocoTestReport/html/index.html`*

See [docs/testing.md](docs/testing.md) for the full testing strategy.

## 🔐 Configuration & Secrets

### Local Secret Management
The project uses the `secrets-gradle-plugin`. To define API keys or sensitive data:

1. Add your key to `local.properties`:
   ```properties
   apiKey="your_api_key_here"
   ```
2. Access it in code:
   ```kotlin
   val apiKey = BuildConfig.apiKey
   ```

### AGP 9.0 Note
`buildConfig` is explicitly enabled in `app/build.gradle.kts` to maintain compatibility with modern plugin standards.

## 📦 Dependencies

| Category | Libraries |
| :--- | :--- |
| **UI** | Jetpack Compose (BOM), Material 3, Coil |
| **DI** | Hilt |
| **Async** | Coroutines, Flow |
| **Networking** | Retrofit, Gson, OkHttp |
| **Testing** | JUnit 4, MockK, MockWebServer, Robolectric, Hilt Testing, Compose Test, Compose Preview Screenshot Testing, Jacoco |

---
Maintainer: [Santiago Mattiauda](https://github.com/santimattius)
