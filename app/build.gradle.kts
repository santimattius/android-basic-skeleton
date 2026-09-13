import com.automattic.android.measure.reporters.SlowSlowTasksMetricsReporter

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.detekt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.secrets.gradle.plugin)
    alias(libs.plugins.automattic.measure.builds)
    alias(libs.plugins.screenshot)
    jacoco
}

android {
    namespace = "com.santimattius.basic.skeleton"
    compileSdk = extraString("target_sdk_version").toInt()

    defaultConfig {
        applicationId = extraString("application_id")
        minSdk = extraString("min_sdk_version").toInt()
        targetSdk = extraString("target_sdk_version").toInt()
        versionCode = extraString("version_code").toInt()
        versionName = extraString("version_name")

        testInstrumentationRunner = "com.santimattius.basic.skeleton.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    experimentalProperties["android.experimental.enableScreenshotTest"] = true

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
        unitTests.all {
            testCoverage {
                version = "0.8.12"
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

// Ensure compatibility with Kotlin 2.x
kotlin {
    jvmToolchain(17)
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
}

fun extraString(key: String): String {
    return (extra[key] as? String) ?: ""
}

detekt {
    config.setFrom("${project.rootDir}/config/detekt/detekt.yml")
    baseline = file("$rootDir/detekt-baseline.xml")
    autoCorrect = true
}

jacoco {
    toolVersion = "0.8.12"
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf(
        "**/R.class",
        "**/R\$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        "**/*_Factory.*",
        "**/*_MembersInjector.*",
        "**/Hilt_*.*",
        "**/*_HiltModules*.*",
        "**/dagger/hilt/**",
    )
    val kotlinDebugTree = fileTree(
        layout.buildDirectory.dir("intermediates/built_in_kotlinc/debug/compileDebugKotlin/classes")
    ) {
        exclude(fileFilter)
    }

    sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))
    classDirectories.setFrom(files(kotlinDebugTree))
    executionData.setFrom(fileTree(layout.buildDirectory) {
        include("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
    })
}

measureBuilds {
    enable = true
    attachGradleScanId =
        false // `false`, if no Enterprise plugin applied OR don't want to attach build scan id
    onBuildMetricsReadyListener {
        SlowSlowTasksMetricsReporter.report(this)
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)

    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    debugImplementation(libs.bundles.compose.debug)

    implementation(libs.bundles.coroutine)
    testImplementation(libs.coroutine.test)
    implementation(libs.bundles.retrofit)
    implementation(libs.gson.core)
    testImplementation(libs.mockwebserver)

    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation)
    ksp(libs.hilt.compiler)

    implementation(libs.coil.core)

    debugImplementation(libs.leakcanary.android)

    testImplementation(platform(libs.compose.bom))
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.robolectric)
    testImplementation(libs.test.ext)
    testImplementation(libs.compose.ui.test.junit)

    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit)
    androidTestImplementation(libs.test.ext)
    androidTestImplementation(libs.test.espresso)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)

    screenshotTestImplementation(platform(libs.compose.bom))
    screenshotTestImplementation(libs.screenshot.validation.api)
    screenshotTestImplementation(libs.compose.tooling)
}
