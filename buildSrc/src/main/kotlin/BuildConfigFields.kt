import com.android.build.gradle.internal.dsl.BaseFlavor
import com.android.build.gradle.internal.dsl.BuildType

// For flavors usage
fun BaseFlavor.buildConfigBoolean(name: String, value: Boolean) =
    buildConfigField("Boolean", name, value.toString())

fun BaseFlavor.buildConfigString(name: String, value: String) =
    buildConfigField("String", name, "\"$value\"")

// For build types usage
fun BuildType.buildConfigString(name: String, value: String) =
    buildConfigField("String", name, "\"$value\"")