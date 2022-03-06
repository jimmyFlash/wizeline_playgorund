import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import java.io.FileInputStream
import java.util.*
import configuration.getConfiguration


const val versionMajor = 1
const val versionMinor = 0
const val versionPatch = 0

fun getCustomProperty (path :String): String {
    val property:String
    property = try {
        val fis = FileInputStream(path)
        val prop = Properties()
        prop.load(fis)
        prop.getProperty("config_app_name")
    } catch (e: java.io.FileNotFoundException) {
        System.err.println("📜 .properties file not found")
        ""
    }
    return property
}


fun BaseAppModuleExtension.setAppConfig() {
    compileSdkVersion(AndroidSDK.compileSdk)
    buildToolsVersion(AndroidSDK.buildTools)

    defaultConfig {
        minSdkVersion(DefaultConfig.minSdk)
        targetSdkVersion(DefaultConfig.targetSdk)

        applicationId = DefaultConfig.applicationID
        versionCode = versionMajor * 100000 + versionMinor * 1000 + versionPatch * 10
        versionName  = "${versionMajor}.${versionMinor}.${versionPatch}"
        testInstrumentationRunner = DefaultConfig.instrumentationRunner

        vectorDrawables.useSupportLibrary = true
        buildConfigString("APP_NAME", getCustomProperty ("./config/common.properties"))
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

fun BaseExtension.setDefaultConfig() {
    compileSdkVersion(AndroidSDK.compileSdk)
    buildToolsVersion(AndroidSDK.buildTools)

    defaultConfig {
        minSdkVersion(DefaultConfig.minSdk)
        targetSdkVersion(DefaultConfig.targetSdk)

        testInstrumentationRunner = TestLibs.runner
    }
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


fun BaseExtension.useDefaultBuildTypes() = buildTypes {
    getByName(BuildTypes.release) {
        isMinifyEnabled = false
        proguardFiles(
            getDefaultProguardFile(ProgaurdFile.textFile), ProgaurdFile.ruleFile )
        resValue ("string", "app_name", getAppName(BuildTypeNum.RELEASE))
        signingConfig = signingConfigs.getByName("release")
        val configuration = getConfiguration()
        setManifestPlaceholders(mapOf("secret" to configuration.secret))
    }

    getByName(BuildTypes.debug) {
        applicationIdSuffix  = ".debug"
        resValue ("string", "app_name", getAppName(BuildTypeNum.DEBUG))
        signingConfig = signingConfigs.getByName("debug")
        val configuration = getConfiguration()
        setManifestPlaceholders(mapOf("secret" to configuration.secret))
    }
}