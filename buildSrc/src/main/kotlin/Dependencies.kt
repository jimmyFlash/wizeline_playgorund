const val  kotlinVersion     = "1.3.71"
const val gradleVersion      = "4.1.0"

object AndroidSDK {
    const val compileSdk = 29
    const val buildTools = "29.0.2"
}

object ProgaurdFile {
    const val textFile = "proguard-android-optimize.txt"
    const val ruleFile = "proguard-rules.pro"
}

object Dagger {
    private object Versions{
        const val dagger      = "2.25.2"
    }
    const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    const val dagger_compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    //const val dagger_android = "com.google.dagger:dagger-android-support:${Versions.dagger}"
    //const val dagger_android_processor =  "com.google.dagger:dagger-android-processor:${Versions.dagger}"

}

object DefaultConfig {
    const val applicationID = "com.wizeline.bookchallenge"
    const val minSdk = 16
    const val targetSdk = 29
    const val versionCode = 1
    const val versionName = "1.0.0"

    const val instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}

object BuildPlugins {

    const val androidGradlePlugin = "com.android.tools.build:gradle:$gradleVersion"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"

    const val taskTypeClean = "clean"

    const val androidApplication = "com.android.application"
    const val kotlinAndroid = "android"
    const val kotlinAndroidExtensions = "android.extensions"
    const val kotlinKapt = "kapt"

//    private const val mavenPushVersion = "3.6.3"
//    const val mavenPush = "digital.wup.android-maven-publish"

}

object BuildTypes {
    const val release = "release"
    const val debug = "debug"
}

object Timber{
    private object Versions{
        const val timber      = "4.5.1"
    }
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
}

object AndroidX{
    private object Versions{
        const val architecture= "1.1.1"
        const val constraintlayout= "1.1.3"
        const val recyclerview= "1.1.0"
        const val lifecyclextensions= "2.2.0"
        const val appcompat      = "1.0.2"
        const val ktx         = "1.0.2"
    }
    const val android_app_compat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val android_constrain_layout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"
    const val android_recyclerview =
        "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
    const val android_lifecycle_extensions =
        "androidx.lifecycle:lifecycle-extensions:${Versions.lifecyclextensions}"
    const val android_core_ktx = "androidx.core:core-ktx:${Versions.ktx}"
}

object KotlinLibs{
    private object Versions{
        const val coroutines  = "1.3.0"
        const val viewmodelKtx = "2.2.0"
    }
    const val kotlin_lib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion"
    const val kotlin_coroutines_core =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val kotlin_coroutine_android =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val kotlin_viewmodel_ktx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.viewmodelKtx}"
}

object TestLibs {
    private object Versions{
        const val jUnit = "4.12"
        const val runner = "1.1.1"
        const val espresso = "3.1.1"
    }
    const val junit  = "junit:junit:${Versions.jUnit}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val runner = "androidx.test:runner:${Versions.runner}"
}

object Lints{
    private object Versions{
        const val lintVersion = "27.0.0"
    }

    // Lint
    const val  lintApi=  "com.android.tools.lint:lint-api:${Versions.lintVersion}"
    const val  lintChecks=  "com.android.tools.lint:lint-checks:${Versions.lintVersion}"
    // Lint Testing
    const val  lint =  "com.android.tools.lint:lint:${Versions.lintVersion}"
    const val  lintTests=  "com.android.tools.lint:lint-tests:${Versions.lintVersion}"
}
