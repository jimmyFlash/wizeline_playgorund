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
                """<?xml version="1.0" encoding="utf-8"?>
                <merge>
                    <RadioButton
                                android:layout_width="wrap_content"
                                android:layout_height="wrap_content"
                                app:layout_constraintBottom_toBottomOf="parent"
                                app:layout_constraintEnd_toEndOf="parent"
                                app:layout_constraintStart_toStartOf="parent"
                                app:layout_constraintTop_toTopOf="parent"
                                />
                </merge>
            """
            ).indented())
            ?.run()
            ?.expectWarningCount(1)
            ?.verifyFixes()
            ?.checkFix(
                null,
                xml(
                    "res/layout/layout_lint_test.xml",
                    """
                <merge>
                     <com.wizeline.bookchallenge.CustomRadioButton
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        app:layout_constraintBottom_toBottomOf="parent"
                        app:layout_constraintEnd_toEndOf="parent"
                        app:layout_constraintStart_toStartOf="parent"
                        app:layout_constraintTop_toTopOf="parent" />
                </merge>
            """
                ).indented()
            )
    }

    override fun getDetector(): Detector = RadioButtonDetector()

    override fun getIssues(): List<Issue> = listOf(RadioButtonDetector.ISSUE)

}