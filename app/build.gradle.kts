plugins {
    androidApp()
    kotlinAndroid()
    kotlinKapt()
    parcelize()
//    ktlint(includeVersion = false)
//    detekt(includeVersion = false)

    /*The safe args plugin ensures type-safe navigations and passing of arguments between destinations.
This plugin generates simple object and builder classes for navigation.*/
    navigationSafeArgs()
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

    implementation (Dagger.dagger)
    kapt (Dagger.dagger_compiler)

    // Navigation Component
    implementation (AndroidX.navigationComp)
    implementation (AndroidX.navigationUi)

    implementation(ImageLibs.picasso)

    implementation(MaterialComponents.material)

    implementation (FaceBook.stetho)

    // added dependencies for the annotation module
    implementation(project(":annotation"))

    // using kapt (Kotlin annotation processor) for codegen module which is our custom
    //annotation processor
    kapt(project(":codegen"))

    implementation(project(":modules:rxandroid"))

    lintChecks(project(":customlints"))


}

//ktlint {
//    android.set(true)
//}