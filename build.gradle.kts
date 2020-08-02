import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
// Top-level build file where you can add configuration options common to all sub-projects/modules.

//defaultTasks 'dependencyUpdates'

buildscript {
    repositories {
        google()
        jcenter()
        
    }
    dependencies {
        classpath(BuildPlugins.androidGradlePlugin)
        classpath(BuildPlugins.kotlinGradlePlugin)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
        classpath ("com.github.ben-manes:gradle-versions-plugin:0.29.0")


    }
}

apply (plugin =  "com.github.ben-manes.versions")

allprojects {
    repositories {
        google()
        jcenter()
        
    }
}

tasks.register(BuildPlugins.taskTypeClean, Delete::class) {
    delete(rootProject.buildDir)
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
