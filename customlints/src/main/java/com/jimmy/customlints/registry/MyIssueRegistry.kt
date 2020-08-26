package com.jimmy.customlints.registry

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue
import com.jimmy.customlints.AndroidLogDetector
import com.jimmy.customlints.RadioButtonDetector
import com.jimmy.customlints.StringContainDetector

/**
 * contains the central list of issues that the Linter references while checking the codebase
 */
class MyIssueRegistry  : IssueRegistry(){

    override val api: Int = CURRENT_API

  override val issues: List<Issue>
   get() = listOf(
       RadioButtonDetector.ISSUE,
       AndroidLogDetector.ISSUE,
       StringContainDetector.ISSUE
   )
 }
