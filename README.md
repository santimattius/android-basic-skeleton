[![codecov](https://codecov.io/gh/santimattius/android-basic-skeleton/branch/master/graph/badge.svg?token=HNW9TXKMQU)](https://codecov.io/gh/santimattius/android-basic-skeleton) ![Quality Checks](https://github.com/santimattius/android-basic-skeleton//actions/workflows/main.yml/badge.svg)

# Android Basic Skeleton

This is basic android project with essential configurations for  app develop in android.

## Verification

Run check project:

```shell
> ./gradlew check
```

Run tests project:

```shell
> ./gradlew test
```

## DeteKt

```shell
> ./gradlew :app:detekt
```

## Coverage

Debug:

```shell
> ./gradlew :app:testDebugUnitTestCoverage
```

Release:

```shell
> ./gradlew :app:testReleaseUnitTestCoverage
```

## Firebase

Create your Firebase project, check here: https://firebase.google.com/docs/android/setup.

Project variation with Firebase Crashlytics, [here](https://github.com/santimattius/android-basic-skeleton/tree/with_crashlitycs)


## Dependencies

Below you will find the libraries used to build the template and according to my criteria the most
used in android development so far.

- **[Hilt](https://developer.android.com/training/dependency-injection/hilt-android)**, dependencies provider.
- **[Retrofit](https://square.github.io/retrofit/)**, networking.
- **[Gson](https://github.com/google/gson)**, json parser.
- **[Coil](https://coil-kt.github.io/coil/compose/)**, with image loader.
- **[Kotlin coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html)**.
- **[Mockk](https://mockk.io/)**, testing library.
- **[MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver)**, networking testing library.
- **[Robolectric](http://robolectric.org/)**, android testing library.
