plugins {
    androidLibrary()
    kotlinAndroid()
    kotlinKapt()

}
android {

    compileSdkVersion(AndroidSDK.compileSdk)
    buildToolsVersion(AndroidSDK.buildTools)

    defaultConfig {
        minSdkVersion(DefaultConfig.minSdk)
        targetSdkVersion(DefaultConfig.targetSdk)

    }

  /*  buildFeatures {
        dataBinding = true
        viewBinding = true
    }
*/

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // For Kotlin projects
    kotlinOptions {
        jvmTarget = "1.8"
    }

}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation(TestLibs.junit)
    androidTestImplementation(TestLibs.runner)
    androidTestImplementation(TestLibs.espresso)

    implementation (KotlinLibs.kotlin_lib)
    implementation (KotlinLibs.kotlin_coroutines_core)
    implementation (KotlinLibs.kotlin_coroutine_android)
    implementation (KotlinLibs.kotlin_viewmodel_ktx)

    implementation (AndroidX.android_app_compat)
    implementation (AndroidX.android_constrain_layout)
    implementation (AndroidX.android_recyclerview)
    implementation (AndroidX.android_lifecycle_extensions)
    implementation (AndroidX.android_core_ktx)
    implementation (AndroidX.lifecycle_runtime_ktx)
    implementation (AndroidX.android_fragment_ktx)

    implementation (RxJava.rxJava3Android)
    implementation (RxJava.rxJava3Kotlin)

    implementation (DataStorage.jetpackRoom)

    implementation (DataStorage.jetpackRoomRxJava3Support)
    kapt(DataStorage.jetpackRoomCompiler)

    implementation (FaceBook.stetho)

    implementation (Dagger.dagger)
    kapt (Dagger.dagger_compiler)
}