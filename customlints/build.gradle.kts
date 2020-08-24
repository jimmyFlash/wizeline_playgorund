plugins {
    javaLibrary()
    kotlinPlugin()
    androidLint()
}


dependencies {
    compileOnly(KotlinLibs.kotlin_lib)
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation(TestLibs.junit)

    // Lint
    compileOnly(Lints.lintApi)
    compileOnly(Lints.lintChecks)
    // Lint Testing
    testImplementation(Lints.lint)
    testImplementation(Lints.lintTests)


}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Lint-Registry-v2"] = "com.jimmy.customlints.registry.MyIssueRegistry"
    }
}

