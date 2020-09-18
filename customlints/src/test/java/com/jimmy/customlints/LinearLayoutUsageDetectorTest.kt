package com.jimmy.customlints

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import org.junit.Test

class LinearLayoutUsageDetectorTest :LintDetectorTest() {

    @Test
    fun testLinearLayoutUsage (){
        lint().files(
            xml(
                "res/layout/ll_layout_test.xml",
                """
                    <LinearLayout
                        xmlns:android="http://schemas.android.com/apk/res/android"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:orientation="vertical">
                    </LinearLayout>
                    """
            ))
            .run()
            .expectWarningCount(1)
    }


    override fun getDetector(): Detector = LinearLayoutUsageDetector()

    override fun getIssues(): MutableList<Issue> = mutableListOf(LinearLayoutUsageDetector.ISSUE)
}