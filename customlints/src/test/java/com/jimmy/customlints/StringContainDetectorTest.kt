package com.jimmy.customlints

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import org.junit.Test

class StringContainDetectorTest: LintDetectorTest() {

    @Test
    fun testBasic() {
        lint().files(
            java("""
                    package test.pkg;
                    public class TestClass1 {
                        // In a comment, mentioning "lint" has no effect
                        private static String s1 = "Ignore non-word usages: linting";
                        private static String s2 = "Let's say it: lint";
                    }
                    """
            ).indented())
            .run()
            .expectWarningCount(1)
            .expect("""
                    src/test/pkg/TestClass1.java:5: Warning: This code mentions lint: Congratulations [ShortUniqueId]
                        private static String s2 = "Let's say it: lint";
                                                   ~~~~~~~~~~~~~~~~~~~~
                    0 errors, 1 warnings
                    """
            )
    }
    override fun getDetector(): Detector = StringContainDetector()


    override fun getIssues(): List<Issue> = listOf(StringContainDetector.ISSUE)
}