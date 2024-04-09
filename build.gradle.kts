@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.google.secrets.gradle.plugin) apply false
    alias(libs.plugins.automattic.measure.builds) apply false
}
true

buildscript {
    dependencies {
        classpath(libs.dep.google.secrets.gradle.plugin)
    }
}