package com.jimmy.customlints

import com.android.tools.lint.detector.api.*
import org.w3c.dom.Element

/**
 * A detector is able to find a particular problem. Each problem type is uniquely
 * identified as an Issue.
 *
 * As we want to scan the XML files to find the usage of RadioButton we will use XmlScanner,
 * define the text reference we are looking for in an XML and the error message
 */
class RadioButtonDetector : Detector(), XmlScanner {

    override fun getApplicableElements(): Collection<String>? {
        return listOf("RadioButton") //  will look for Radio Button //Text in all xml
    }

    override fun visitElement(context: XmlContext, element: Element) {

        // the quick fix suggestion supplied by the context menu
        val idiotRadioButtonFix = LintFix.create()
            .name("Use IdiotRadioButton")
            .replace()
            .text("RadioButton")
            .with("com.wizeline.bookchallenge.CustomRadioButton")
            .robot(true)
            .independent(true)
            .build()

        // report to display for the found issue
        context.report(
            issue = ISSUE,
            location = context.getNameLocation(element),
            message = "Usage of Radio Button is prohibited",
            quickfixData = idiotRadioButtonFix
        )
    }

    companion object {

//        An issue is a potential bug in an Android application.
//        An issue is discovered by a Detector, and has an associated Severity.
        val ISSUE = Issue.create(
            id = "IdiotRadioButtonUsageWarning", // It’s a unique identifier for the issue and the one we mention in @SupressWarning(“”).
            briefDescription = "Android's RadioButton should not be used", // lines to explain the issue and will be shown for the lint, typically describing the problem rather than the fix.
            explanation = "Don't use Android Radio button, be an idiot and use IdiotRadioButton instead",
            category = Category.CORRECTNESS, // A category is a container for related issues.
            priority = 3, // It is defined on a scale of 1–10 which we can use to determine which issues to fix first.
            severity = Severity.WARNING, //  Determines if a lint would be treated as a warning or an error while building the app. An error will cause the build to break while a warning would be just printed to console or a file
            implementation = Implementation(  // Scope of the issue that it is interested in like manifest, resource files, java/Kotlin source files
                RadioButtonDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE
            )
        )
    }

}

