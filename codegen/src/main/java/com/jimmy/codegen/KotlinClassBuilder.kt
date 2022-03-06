package com.jimmy.codegen

/**
 * Custom Kotlin Class Builder which returns file content string
 * Use KotlinPoet for production app
 * KotlinPoet can be found at https://github.com/square/kotlinpoet
 */
class KotlinClassBuilder(className: String,
                         packageName:String,
                         greeting:String = "Merry Christmas!!"){

    // used String templating and generated
    //the above string by passing the appropriate package Name, class name,
    //and greeting
    private val contentTemplate = """
        package $packageName
        class $className {
        fun greeting() = "$greeting"
        }
        """.trimIndent()

    fun getContent() : String{
        return contentTemplate
    }
}