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
        const val dagger      = "2.40.1"
    }
    const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    const val dagger_compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    //const val dagger_android = "com.google.dagger:dagger-android-support:${Versions.dagger}"
    //const val dagger_android_processor =  "com.google.dagger:dagger-android-processor:${Versions.dagger}"

}

object DefaultConfig {
    const val applicationID = "com.wizeline.bookchallenge"
    const val minSdk = 21
    const val targetSdk = 29
    const val versionCode = 1
    const val versionName = "1.0.0"

    const val instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}

object BuildPlugins {

    object Version {
        const val navigationSafeArgs = "1.0.0"
    }

    const val navigationSafeArgsPlugin =
        "android.arch.navigation:navigation-safe-args-gradle-plugin:${Version.navigationSafeArgs}"

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
        const val constraintlayout = "2.1.2"
        const val recyclerview = "1.2.1"
        const val lifecyclextensions = "2.2.0"
        const val appcompat      = "1.3.1"
        const val ktx         = "1.6.0"
        const val lifecycleRuntimeKtx  = "2.3.1"
        const val navigationKtx = "2.3.5"
        const val fragmentKtx = "1.4.0"
    }

    const val android_app_compat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val android_constrain_layout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"
    const val android_recyclerview =
        "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
    const val android_lifecycle_extensions =
        "androidx.lifecycle:lifecycle-extensions:${Versions.lifecyclextensions}"
    const val android_core_ktx = "androidx.core:core-ktx:${Versions.ktx}"
    const val lifecycle_runtime_ktx =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntimeKtx}"

    const val navigationComp = "androidx.navigation:navigation-fragment-ktx:${Versions.navigationKtx}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigationKtx}"

    const val android_fragment_ktx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtx}"
}

object KotlinLibs{
    private object Versions{
        const val coroutines  = "1.5.2"
        const val viewmodelKtx = "2.3.1"
        const val  kotlinVersion     = "1.4.32"
    }

    // TODO: 10/29/2021 move the below 2 lines to main build.gradle.kts and refactor affected project modules .kts
    const val kotlin_lib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlinVersion}"
    // jdk8 coroutines integration module
    const val kotlin_lib_jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlinVersion}"
    const val kotlin_coroutines_core =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val kotlin_coroutine_android =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val kotlin_viewmodel_ktx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.viewmodelKtx}"
}

object TestLibs {
    private object Versions{
        const val jUnit = "4.13.2"
        const val runner = "1.4.0"
        const val espresso = "3.4.0"
    }
    const val junit  = "junit:junit:${Versions.jUnit}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val runner = "androidx.test:runner:${Versions.runner}"
}

object ImageLibs{
    private object Versions{
        const val picasso_version = "2.71828"
    }
    const val picasso  = "com.squareup.picasso:picasso:${Versions.picasso_version}"
}

object MaterialComponents{
    private object Versions{
        const val material_version = "1.4.0"
    }
    const val material  = "com.google.android.material:material:${Versions.material_version}"
}

// for code generation in the annotation processor
object GoogleAutoService{
    private object Versions{
        const val autoService_version = "1.0"
    }
    const val autoService  = "com.google.auto.service:auto-service:${Versions.autoService_version}"
}

object RxJava{
    private object Versions{
        const val rx_kotlin_version = "3.0.1"
        const val rx_android_version = "3.0.0"
    }

    const val rxJava3Android = "io.reactivex.rxjava3:rxandroid:${Versions.rx_android_version}"
    const val rxJava3Kotlin = "io.reactivex.rxjava3:rxkotlin:${Versions.rx_kotlin_version}"
}

object DataStorage{
    private object Versions{
        const val room_version = "2.3.0"
    }
    const val jetpackRoom  = "androidx.room:room-runtime:${Versions.room_version}"
    const val jetpackRoomCompiler  = "androidx.room:room-compiler:${Versions.room_version}"
    const val jetpackRoomRxJava3Support = "androidx.room:room-rxjava3:${Versions.room_version}"
}

object FaceBook{
    private object Versions{
        const val stetho_version = "1.5.1"
    }
    const val stetho  = "com.facebook.stetho:stetho:${Versions.stetho_version}"
}