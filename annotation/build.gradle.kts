import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    javaLibrary()
    kotlinPlugin()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}


repositories{
    mavenCentral()
}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation (KotlinLibs.kotlin_lib)
}

val compileKotlin: KotlinCompile by tasks
val compileTestKotlin : KotlinCompile by tasks

compileKotlin.kotlinOptions{
    jvmTarget = "1.8"
}

compileTestKotlin.kotlinOptions{
    jvmTarget = "1.8"
}