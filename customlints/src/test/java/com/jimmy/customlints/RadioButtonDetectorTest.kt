package com.jimmy.customlints

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.LintDetectorTest.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import org.junit.Test

class RadioButtonDetectorTest : LintDetectorTest() {
    @Test
    fun testRadioButton() {

        lint()?.files(
            xml(
                "res/layout/layout_lint_test.xml",
                """
                        <RadioButton
                            xmlns:android="http://schemas.android.com/apk/res/android"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                        />
                        """
            ))
            ?.run()
            ?.expectWarningCount(1)
            ?.verifyFixes()
            ?.checkFix(
                null,
                xml(
                    "res/layout/layout_lint_test.xml",
                    """
                        <com.wizeline.bookchallenge.CustomRadioButton
                            xmlns:android="http://schemas.android.com/apk/res/android"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                        />
                        """
                ))
    }

    override fun getDetector(): Detector = RadioButtonDetector()

    override fun getIssues(): List<Issue> = listOf(RadioButtonDetector.ISSUE)

}