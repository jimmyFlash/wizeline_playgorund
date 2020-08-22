package com.jimmy.customlints

import com.android.tools.lint.checks.infrastructure.LintDetectorTest.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class MyIssueDetectorTest {
    @Test
    fun testRadioButton() {

        lint()?.files(
            xml(
                "res/layout/layout_lint_test.xml",
                """
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
            ).indented()
        )
            ?.issues(RadioButtonDetector.ISSUE)
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
}