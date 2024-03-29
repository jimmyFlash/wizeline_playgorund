import org.gradle.kotlin.dsl.kotlin
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

fun PluginDependenciesSpec.androidApp(): PluginDependencySpec =
    id("com.android.application")

fun PluginDependenciesSpec.androidLibrary(): PluginDependencySpec =
    id("com.android.library")

fun PluginDependenciesSpec.javaLibrary(): PluginDependencySpec =
    id("java-library")

fun PluginDependenciesSpec.androidLint(): PluginDependencySpec =
    id("com.android.lint")

fun PluginDependenciesSpec.kotlinPlugin(): PluginDependencySpec =
    id("kotlin")

fun PluginDependenciesSpec.parcelize(): PluginDependencySpec =
    id("kotlin-parcelize")


fun PluginDependenciesSpec.kotlinAndroid(): PluginDependencySpec =
    kotlin("android")

fun PluginDependenciesSpec.kotlinAndroidExt(): PluginDependencySpec =
    kotlin("android.extensions")

fun PluginDependenciesSpec.kotlinKapt(): PluginDependencySpec =
    kotlin("kapt")

fun PluginDependenciesSpec.navigationSafeArgs(): PluginDependencySpec =
    id("androidx.navigation.safeargs")

fun PluginDependenciesSpec.dependencyUpdates(): PluginDependencySpec =
    id("com.github.ben-manes.versions").version("0.39.0")

// plugin that removes unused dependencies / annotations and check dependency implementation method
// with output report
fun PluginDependenciesSpec.buildHealth(includeVersion: Boolean = true): PluginDependencySpec =
    id("com.autonomousapps.dependency-analysis").also { if (includeVersion) it.version("0.54.0") }

fun PluginDependenciesSpec.ktlint(includeVersion: Boolean = true): PluginDependencySpec =
    id("org.jlleitschuh.gradle.ktlint").also { if (includeVersion) it.version("8.2.0") }

fun PluginDependenciesSpec.detekt(includeVersion: Boolean = true): PluginDependencySpec =
    id("io.gitlab.arturbosch.detekt").also { if (includeVersion) it.version("1.1.1") }