

plugins {
    javaLibrary()
    kotlin()
    kotlinKapt()
}


java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

kapt{
    generateStubs = true
}

sourceSets{
    main{
        java {
            srcDir("${buildDir.absolutePath}/tmp/kapt/main/kotlinGenerated/")
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation (KotlinLibs.kotlin_lib)
    kapt(project(":annotation"))
    compileOnly(project(":annotation"))
    // added AutoService dependencies to link services
    implementation(GoogleAutoService.autoService)
}