import java.io.FileInputStream
import java.util.*

plugins {
    androidApp()
    kotlinAndroid()
    kotlinAndroidExt()
    kotlinKapt()
}

fun getCustomProperty (path :String): String {
    val property:String
    property = try {
        val fis = FileInputStream(path)
        val prop = Properties()
        prop.load(fis)
        prop.getProperty("config_app_name")
    } catch (e: java.io.FileNotFoundException) {
        ""
    }
    return property
}


enum class BuildTypeNum {
    DEBUG,
    RELEASE
}

fun  getAppName( buildType : BuildTypeNum) : String {
    val  appName = "WiseLine playground - "

    return when (buildType) {
        BuildTypeNum.DEBUG ->
            "$appName  ${BuildTypes.debug}"
        BuildTypeNum.RELEASE ->
            "$appName  ${BuildTypes.release}"
    }
}


val versionMajor = 1
val versionMinor = 0
val versionPatch = 0




android {
    compileSdkVersion(AndroidSDK.compileSdk)
    buildToolsVersion(AndroidSDK.buildTools)
    defaultConfig {
        applicationId = DefaultConfig.applicationID
        minSdkVersion(DefaultConfig.minSdk)
        targetSdkVersion(DefaultConfig.targetSdk)
        versionCode = versionMajor * 100000 + versionMinor * 1000 + versionPatch * 10
        versionName  = "${versionMajor}.${versionMinor}.${versionPatch}"
        testInstrumentationRunner = DefaultConfig.instrumentationRunner

        buildConfigString("APP_NAME", getCustomProperty ("./config/common.properties"))
    }
    buildTypes {
        getByName(BuildTypes.release) {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile(ProgaurdFile.textFile), ProgaurdFile.ruleFile )
            resValue ("string", "app_name", getAppName(BuildTypeNum.RELEASE))
        }

        getByName(BuildTypes.debug) {
            applicationIdSuffix  = ".debug"
            resValue ("string", "app_name", getAppName(BuildTypeNum.DEBUG))
        }

    }



    buildFeatures{
        dataBinding = true
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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

    implementation (Dagger.dagger)
    kapt (Dagger.dagger_compiler)

}
