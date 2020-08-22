package com.jimmy.customlints

import com.android.tools.lint.checks.infrastructure.LintDetectorTest.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class AndroidLogIssueTest {
    @Test
    fun bark() {
        lint()?.files(
            java("""  
                class Test {
                    fun main(view: View) {
                       Log.d(TAG, "woof! woof!")
                    }
                }
            """.trimIndent())
        )
        ?.issues(AndroidLogDetector.ISSUE)
        ?.run()// run analyzer
        ?.expectWarningCount(1)  // make assertions

    }
    companion object {
        private const val TAG = "Sample"
    }
}