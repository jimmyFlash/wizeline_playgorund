import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins{
    dependencyUpdates()
//    ktlint()
//    detekt()
//    buildHealth()
}

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        
    }
    dependencies {
//        classpath(BuildPlugins.androidGradlePlugin)
//        classpath(BuildPlugins.kotlinGradlePlugin)
        classpath(BuildPlugins.navigationSafeArgsPlugin)
    }
}


allprojects {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        
    }
}

tasks.register(BuildPlugins.taskTypeClean, Delete::class) {
    delete(rootProject.buildDir)
}

task<tasks.PingUrlTask>("pingUrl") {
    url = "https://www.google.com"
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.named("dependencyUpdates", DependencyUpdatesTask::class.java).configure {
    // Example 2: disallow release candidates as upgradable versions from stable versions
    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }

    // optional parameters
    checkForGradleUpdate = true
    outputFormatter = "json"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"
}
