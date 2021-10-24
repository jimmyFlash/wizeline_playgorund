plugins {
    androidApp()
    kotlinAndroid()
    kotlinAndroidExt()
    kotlinKapt()
//    ktlint(includeVersion = false)
//    detekt(includeVersion = false)

}
android {
    setDefaultSigningConfigs(project)
    setAppConfig()
    useDefaultBuildTypes()


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
}