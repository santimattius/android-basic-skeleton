# Android Basic Skeleton

[![Codecov](https://codecov.io/gh/santimattius/android-basic-skeleton/branch/master/graph/badge.svg?token=HNW9TXKMQU)](https://codecov.io/gh/santimattius/android-basic-skeleton)
[![Quality Checks](https://github.com/santimattius/android-basic-skeleton/actions/workflows/main.yml/badge.svg)](https://github.com/santimattius/android-basic-skeleton/actions)
[![AGP 9.1.0](https://img.shields.io/badge/AGP-9.1.0-blue.svg)](https://developer.android.com/build/releases/gradle-plugin)
[![Kotlin 2.2.10](https://img.shields.io/badge/Kotlin-2.2.10-purple.svg)](https://kotlinlang.org/docs/whatsnew20.html)

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
│   └── build.gradle.kts  # App-specific build configuration
├── config/               # Configuration files (Detekt, etc.)
├── gradle/               # Gradle scripts and version catalog
│   ├── coverage.gradle   # Jacoco coverage reporting logic
│   └── libs.versions.toml # Centralized dependency management
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

Execute unit tests:
```bash
./gradlew test
```

Run static analysis (Detekt):
```bash
./gradlew :app:detekt
```

### Code Coverage Reports

Generate Jacoco coverage for debug builds:
```bash
./gradlew :app:testDebugUnitTestCoverage
```
*Reports are generated at: `app/build/reports/jacoco/`*

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
| **Testing** | JUnit 4, Mockk, MockWebServer, Compose Test |

---
Maintainer: [Santiago Mattiauda](https://github.com/santimattius)
