plugins {
    `kotlin-dsl`
}

val kotlinVersion     = "1.3.71"
val gradleVersion      = "4.1.0"
val okhttp      = "4.3.1"

repositories {
    jcenter()
    google()
}

dependencies {
    implementation("com.android.tools.build:gradle:$gradleVersion")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")

    implementation("com.squareup.okhttp3:okhttp:$okhttp")
}
