# Testing Strategy — android-basic-skeleton

> Sections 1–4 are the original analysis and plan. Sections 5–8 describe what was actually implemented, the gotchas hit along the way, and what's still open — see those for the current state.

## 1. Baseline state (before implementation)

Single-module app (`:app`), 100% Jetpack Compose (no XML views).

| Area | Current state |
|---|---|
| DI framework | Hilt 2.60.1. No `HiltTestRunner` / `@HiltAndroidTest` wired up yet — `testInstrumentationRunner` is still the default `AndroidJUnitRunner`. |
| Unit test framework | JUnit4 (4.13.2), both `test` and `androidTest`. |
| Mocking framework | None installed. Network layer is faked with `MockWebServer` instead of mocks (good pattern to keep). |
| Robolectric | Not installed. |
| UI paradigm | 100% Compose (`buildFeatures.compose = true`), no Views/Espresso usage beyond the boilerplate dependency. |
| Behavior UI tests | None real. `androidTest` has only the AS-generated `ExampleInstrumentedTest` (asserts package name). `compose-ui-test-junit4` is declared in `androidTest` but unused. |
| Screenshot tests | None (no Paparazzi, Roborazzi, Dropshots, or Compose Preview Screenshot Testing). |
| End-to-end tests | None (no UI Automator). |
| Coverage | AGP built-in coverage is on (`enableUnitTestCoverage`/`enableAndroidTestCoverage = true`, `testCoverage.version = "0.8.12"` → JaCoCo under the hood), but there's no JaCoCo Gradle plugin/report task configured. |
| Existing test harness | `app/src/test/.../tools/rules/MainCoroutinesTestRule.kt` (swaps `Dispatchers.Main`), `tools/helpers/JsonLoader.kt` (loads JSON fixtures from `src/test/resources`), `mockwebserver/MockWebServerExampleTest.kt` (Retrofit + MockWebServer example). This is a reasonable foundation to build on. |
| Production code under test | `MainViewModel` (state + `sayHello()` business logic), `RetrofitServiceCreator`, `AppModule` (DI wiring), `MainActivity`/`MainScreen` (Compose UI), `ExampleInterceptor`. |

**Takeaway:** the skeleton already has the right *shape* of test tooling (coroutine rule, JSON fixture loader, MockWebServer pattern) but is missing everything needed to test Compose UI behavior, screenshots, and Hilt-integrated instrumented tests. Since this project is a template for new apps, the priority is a *reusable harness*, not coverage of the one existing screen.

## 2. Target stack

| Concern | Tool | Why |
|---|---|---|
| Unit tests | JUnit4 (keep) | Already in place, matches AGP defaults. |
| Coroutines/Flow testing | `kotlinx-coroutines-test` (keep) + existing `MainCoroutinesTestRule` | Already present. |
| Mocking | MockK (`io.mockk:mockk`) | Only add when a fake is impractical (e.g. mocking `Interceptor.Chain`). Prefer fakes for repositories/services. |
| DI in tests | Hilt testing (`hilt-android-testing`) + custom `HiltTestRunner` | Needed for any `androidTest` that exercises `@AndroidEntryPoint`/`@HiltViewModel` classes end to end. |
| Compose behavior tests | `androidx.compose.ui:ui-test-junit4` + Robolectric | Run Compose semantics-based tests on the JVM (`test` sourceset) for speed; keep a thin `androidTest` set only for what truly needs a device (Hilt DI end-to-end, system UI). |
| Screenshot tests | Compose Preview Screenshot Testing (Studio-native, LayoutLib-based, runs on JVM) | Zero extra CI infra, integrates with existing `@Preview`s (`DefaultPreview`). Add Dropshots later only if device-rendering-accurate screenshots become necessary (e.g. edge-to-edge, real fonts). |
| Coverage | JaCoCo Gradle plugin on top of the existing AGP flags | Turns the already-enabled coverage data into an actual report/task in CI. |
| End-to-end | Compose Test APIs (JVM/Robolectric) for app-level journeys; UI Automator only if/when the app needs to touch system UI (permissions, notifications) | Keep E2E to ~5% of tests once the app has real journeys — today there's only one screen, so this tier is a placeholder for later features. |

## 3. Test pyramid for this codebase

```
        E2E (device/UI Automator)         ~5%   — none yet, add when real user journeys exist
   Screenshot tests (Preview screenshot)  ~15%   — MainScreen states (loading/message), AppBar
 Compose behavior tests (Robolectric)     ~25%   — MainRoute/MainScreen interaction + state rendering
Unit tests (ViewModel, DI, networking)    ~55%   — MainViewModel, RetrofitServiceCreator, ExampleInterceptor
```

## 4. Per-component plan

### Unit tests (`src/test`)
- `MainViewModel`: cover `sayHello()` — initial `onStart` trigger, loading flag toggling, both random outcomes (inject a seedable random or extract the `Random` call behind a fake to make the branch deterministic instead of asserting on chance).
- `RetrofitServiceCreator`: already covered indirectly via `MockWebServerExampleTest`; keep that pattern for any new service.
- `ExampleInterceptor`: unit-test in isolation with a fake `Interceptor.Chain` (MockK) or via MockWebServer request assertions.
- `AppModule`: do not unit-test DI wiring directly; validate it via a Hilt instrumented smoke test instead (see below).

### Compose behavior tests (new — put in `src/test`, run via Robolectric)
- Add Robolectric + `ui-test-junit4` to `testImplementation`.
- `MainScreen`: given `MainUiState`, assert loading indicator vs. message text visibility; tapping the FAB invokes `onMainAction`. Prefer semantic matchers (`onNodeWithText`, `onNodeWithContentDescription`) before falling back to `testTag`.
- `MainRoute`: only needs a thin test verifying it wires `viewModel.state` → `MainScreen` and `viewModel::sayHello` → the action callback (can stay light since `MainScreen` carries the real logic).

### Screenshot tests (implemented)
- Compose Preview Screenshot Testing is enabled on `:app` (`app/src/screenshotTest/kotlin/.../MainScreenScreenshotTest.kt`).
- Implemented so far: `MainScreen` message state, loading state, and a font-scale-1.5 variant — 3 representative previews to establish the pattern, not the full 3×3 width/height matrix from the plan. Extending to the full size matrix (and adding `AppBar` component-level screenshots) is a straightforward follow-up once the app has more than one screen worth the coverage.
- `AppBar` is `internal`; the `screenshotTest` source set's friend-path visibility to `internal` declarations wasn't verified, so it isn't screenshot-tested directly — it's covered indirectly through `MainScreen`'s screenshots.

### Instrumented tests (`src/androidTest`) — keep minimal
- Replace the default `AndroidJUnitRunner` with a Hilt-aware runner once any `@HiltAndroidTest` is added.
- One smoke test launching `MainActivity` end to end (real Hilt graph) to catch DI wiring regressions that JVM tests can't (e.g. missing `@Provides`, scoping errors).
- Do not duplicate the Compose behavior tests here — those belong in `src/test` via Robolectric for speed.

### End-to-end
- No dedicated E2E tests yet — there's a single screen with no navigation or cross-screen journeys. Revisit once the app grows beyond the skeleton.

### Coverage
- Add the JaCoCo Gradle plugin to `:app` so the already-enabled `testCoverage` flags produce a real `jacocoTestReport` task in CI, rather than only instrumenting without reporting.

## 5. What's installed

```toml
# gradle/libs.versions.toml
mockk = "1.14.11"
robolectric = "4.16.1"
screenshot = "0.0.1-alpha15"

mockk = { module = "io.mockk:mockk", version.ref = "mockk" }
robolectric = { module = "org.robolectric:robolectric", version.ref = "robolectric" }
hilt_android_testing = { module = "com.google.dagger:hilt-android-testing", version.ref = "hilt" }
screenshot_validation_api = { module = "com.android.tools.screenshot:screenshot-validation-api", version.ref = "screenshot" }
screenshot = { id = "com.android.compose.screenshot", version.ref = "screenshot" }
```

```kotlin
// app/build.gradle.kts
plugins {
    // ...
    alias(libs.plugins.screenshot)
    jacoco
}

android {
    // ...
    experimentalProperties["android.experimental.enableScreenshotTest"] = true
    defaultConfig {
        testInstrumentationRunner = "com.santimattius.basic.skeleton.HiltTestRunner"
    }
}

dependencies {
    testImplementation(libs.mockk)
    testImplementation(libs.robolectric)
    testImplementation(libs.test.ext)
    testImplementation(libs.compose.ui.test.junit)

    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)

    screenshotTestImplementation(platform(libs.compose.bom))
    screenshotTestImplementation(libs.screenshot.validation.api)
    screenshotTestImplementation(libs.compose.tooling)
}
```

`gradle.properties` also needs `android.experimental.enableScreenshotTest=true` (both the project-wide flag and the module `experimentalProperties` entry are required per the tool's setup docs).

### Gotchas hit during implementation

- **`@HiltViewModel` rejects Kotlin default parameter values on the `@Inject` constructor.** The original plan was `MainViewModel(private val random: Random = Random.Default)` for deterministic tests. Hilt's KSP processor failed with *"should contain exactly one @Inject or @AssistedInject annotated constructor"* — the synthetic defaults-bitmask constructor Kotlin generates confuses it. Fix: no default value: `@Inject constructor(private val random: Random)`, with a `@Provides fun provideRandom(): Random = Random.Default` added to `AppModule`. Tests construct `MainViewModel(random)` directly without Hilt (per the Hilt testing guide — constructor-injected classes don't need Hilt in unit tests).
- **`MainCoroutinesTestRule`'s `UnconfinedTestDispatcher()` is not the same scheduler as `runTest`'s by default.** Combined with `StateFlow`'s conflation, this produced flaky/inconsistent intermediate emissions (sometimes the `isLoading = true` transient state was observed, sometimes it was skipped). Fixed by passing the rule's dispatcher into the test body explicitly: `runTest(mainCoroutinesTestRule.testDispatcher) { ... }`, and by asserting on `advanceUntilIdle()`-settled state via a plain `flow.toList()` collector rather than asserting on an exact intermediate-emission sequence (which stayed racy even with a shared scheduler).
- **`createComposeRule()` / `createAndroidComposeRule()` are deprecated** in this Compose BOM (moving to a `v2` API using `StandardTestDispatcher` instead of `Unconfined`). Left as-is since v1 still works and the migration is a separate, non-blocking cleanup.
- **`waitUntilDoesNotExist` is `@ExperimentalTestApi`** — the instrumented smoke test needs `@OptIn(ExperimentalTestApi::class)`.
- **AGP 9's built-in Kotlin support** (`android.builtInKotlin=true`) compiles Kotlin to `build/intermediates/built_in_kotlinc/debug/compileDebugKotlin/classes`, not the classic `build/tmp/kotlin-classes/debug` used by the standalone `org.jetbrains.kotlin.android` plugin. The `jacocoTestReport` task's `classDirectories` points there — get this wrong and JaCoCo silently produces an empty report (no error, just a report with zero classes).
- **Robolectric 4.16.1 supports up to API 36 (Baklava)**, but this project's `targetSdk` is 37. Robolectric-based tests (`MainScreenTest`) pin `@Config(sdk = [34])` to stay within Robolectric's supported range instead of defaulting to `targetSdk`.

## 6. Commands

- Unit + Robolectric tests: `./gradlew :app:testDebugUnitTest`
- Instrumented tests (needs a connected device/emulator — not available in this environment, so these are compile-verified only, not run): `./gradlew :app:connectedDebugAndroidTest`
- Screenshot tests — generate/update baselines: `./gradlew :app:updateDebugScreenshotTest`
- Screenshot tests — verify against baselines: `./gradlew :app:validateDebugScreenshotTest`
- Coverage report: `./gradlew :app:jacocoTestReport` → `app/build/reports/jacoco/jacocoTestReport/html/index.html`
- Static analysis: `./gradlew :app:detekt`

Screenshot reference images live under `app/src/screenshotTestDebug/reference/` (Compose Preview Screenshot Testing default) and are committed to the repo.

## 7. Verification performed

All of the following were run in this environment and passed:
- `:app:compileDebugUnitTestKotlin`, `:app:compileDebugAndroidTestKotlin`, `:app:compileDebugScreenshotTestKotlin` (all three new test source sets compile)
- `:app:testDebugUnitTest` — 11/11 tests pass (`ExampleUnitTest`, `MainViewModelTest`, `MainScreenTest`, `ExampleInterceptorTest`, `MockWebServerExampleTest`)
- `:app:jacocoTestReport` — produces real, non-empty coverage data
- `:app:updateDebugScreenshotTest` + `:app:validateDebugScreenshotTest` — reference images generated and validated with no diffs
- `:app:detekt` — 0 code smells
- `:app:assembleDebug` — production build unaffected

Not run: `:app:connectedDebugAndroidTest` (the Hilt instrumented smoke test) — no device or emulator was connected in this environment. It compiles and its Hilt annotation processing succeeds; running it on a device/emulator or CI is the remaining verification step.

## 8. Remaining follow-ups (not done, not blocking)

- Expand `MainScreen` screenshot tests to the full 3×3 width/height matrix described in section 3 once there's a second screen to justify the boilerplate.
- Run `:app:connectedDebugAndroidTest` on a real device/emulator or in CI to confirm the Hilt smoke test passes at runtime.
- Consider migrating `createComposeRule`/`createAndroidComposeRule` to the `v2` APIs before they're removed.
