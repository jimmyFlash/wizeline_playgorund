package com.jimmy.codegen

import com.google.auto.service.AutoService
import com.jimmy.annotation.GreetingGenerator
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@AutoService(Processor::class) // For registering the service
@SupportedSourceVersion(SourceVersion.RELEASE_8) // to support Java 8
@SupportedOptions(FileGenerator.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class FileGenerator : AbstractProcessor() {

    /**
     * write  our processing logic. Override this method. We receive
     * RoundEnvironment in the method which we will use to get all the
     * elements annotated with a GreetingGenerator annotation
     */
    override fun process(set: MutableSet<out TypeElement>?,
                         roundEnvironment: RoundEnvironment?): Boolean {
        /*
        roundEnvironment?.getElementsAnnotatedWith gives us a list of elements annotated
        with our annotation. Then for each element, we will get class name, package name and then
        generateClass which is our custom function to generate code during
        compile time
         */
        roundEnvironment?.getElementsAnnotatedWith(GreetingGenerator::class.java)
            ?.forEach {
                val className = it.simpleName.toString()
                val pack = processingEnv.elementUtils.getPackageOf(it).toString()
                generateClass(className, pack)
            }
        return true
    }

    /**
     * In this method, we write logic to generate our class. First, we get
     * fileName and use our Custom KotlinClassBuilder class to generate
     * required file content
     */
    private fun generateClass(className: String, pack: String){
        val fileName = "Generated_$className"
        val fileContent = KotlinClassBuilder(fileName,pack).getContent()

        // get the destination directory and create a new file in that
        //directory with previously obtained fileContent
        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
        val file = File(kaptKotlinGeneratedDir, "$fileName.kt")
        file.writeText(fileContent)
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(GreetingGenerator::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }


    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}