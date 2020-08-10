import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.lang.Exception
import java.util.*

fun BaseAppModuleExtension.setDefaultSigningConfigs(project: Project) = signingConfigs {
    getByName("debug") {
        storeFile = project.rootProject.file("debug.keystore")
        storePassword = "android"
        keyAlias = "AndroidDebugKey"
        keyPassword = "android"
    }
    register("release") {
       /* storeFile = project.rootProject.file("wiseLineRelease.jks")
        storePassword = "jimmy5"
        keyAlias = "wiseline"
        keyPassword = "jimmy5"*/
        val keystorePropertiesFile = project.rootProject.file("realse-keystore.properties")
        if (!keystorePropertiesFile.exists()) {
            System.err.println("📜 Missing release-keystore.properties file for release signing")
        } else {
            val keystoreProperties = Properties().apply {
                load(FileInputStream(keystorePropertiesFile))
            }
            try {
//                storeFile = File(keystoreProperties["storeFile"] as String)
                storeFile = project.rootProject.file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
            } catch (e: Exception) {
                System.err.println("📜 release.keystore.properties file is malformed")
            }
        }
    }
}
