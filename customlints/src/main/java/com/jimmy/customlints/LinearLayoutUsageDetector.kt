package com.jimmy.customlints

import com.android.SdkConstants
import com.android.tools.lint.detector.api.*
import org.w3c.dom.Element

/**
 * LayoutDetector class which our detector is extending is just syntactic sugar
 * for extending a Detector and XmlScanner while also scoping the scanner to only visit layout files
 */
class LinearLayoutUsageDetector: LayoutDetector()  {

    /*
     we only want to detect and correct usage of LinearLayout,
      limit our detector to only the instances of LinearLayout
     */
    override fun getApplicableElements(): Collection<String>? {
        return listOf(SdkConstants.LINEAR_LAYOUT)
    }

    override fun visitElement(context: XmlContext, element: Element) {
        context.report(
            issue = ISSUE,
            location = context.getElementLocation(element),
            message = REPORT_MESSAGE,
            quickfixData = computeQuickFix()
        )
    }

    /**
     * quick fix option to apply
     */
    private fun computeQuickFix(): LintFix {
        return LintFix.create()
            .replace().text(SdkConstants.LINEAR_LAYOUT)
            .with(SdkConstants.ANDROIDX_CONSTRAINT_LAYOUT_PKG.plus("widget.ConstraintLayout"))
            .build()
    }

    companion object {

        private const val REPORT_MESSAGE = "Legacy layout detected, use Constrain layout instead"

        private val IMPLEMENTATION = Implementation(LinearLayoutUsageDetector::class.java,
            Scope.RESOURCE_FILE_SCOPE)

        val ISSUE = Issue.create(
            id = "LinearLayoutIssueID", // Every issue has it's own unique id, also used when trying to suppress a lint
            briefDescription = "Use ConstraintLayout instead",
            explanation = "Constraint-layout is more memory efficient and supports animation",
            category = Category.CORRECTNESS, // One out of several options like ICONS, SECURITY, COMPLIANCE, etc.
            priority = 10, // An integer value specifying priority from 1 to 10, with 10 being highest. Used when multiple issues are reported on same element
            severity = Severity.WARNING, // One of the following values FATAL, ERROR, WARNING, INFORMATIONAL, IGNORE
            implementation = IMPLEMENTATION // directing to the implementation of this issue, i.e. the detector class, used when registering this issue
        )
    }


}