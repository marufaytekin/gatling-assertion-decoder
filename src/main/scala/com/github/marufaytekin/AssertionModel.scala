package com.github.marufaytekin

case class TargetNew(name: String, metric: String, statistic: String)
case class ConditionNew(name: String, value: Any, incl: Boolean)
case class AssertionNew(scope: String, groupName: String, requestName: String, targetNew: TargetNew, conditionNew: ConditionNew)

