package com.github.marufaytekin

import java.nio.ByteBuffer
import java.util.Base64

import boopickle.Default.Unpickle
import io.gatling.commons.stats.assertion._

object Decoder {

  def getRequestName(parts: List[String]): String = parts match {
    case List(a, b) => a + "/" + b
    case List(a) => a
    case List() => null
  }

  def getPath(path: AssertionPath): (String, Any) = path match {
    case Global => ("Global", null)
    case ForAll => ("ForAll", null)
    case Details(parts) => ("Details", getRequestName(parts))
  }

  def getTarget(assertion: Assertion): (Object, Printable, TimeSelection) =
    assertion.target match {
      case MeanRequestsPerSecondTarget =>
        (MeanRequestsPerSecondTarget, null, null)
      case target: CountTarget =>
        (CountTarget, target.metric, null)
      case target: PercentTarget =>
        (PercentTarget, target.metric, null)
      case target: TimeTarget =>
        (TimeTarget, target.metric, target.selection)
    }

  def getCondition(condition: Condition): ConditionNew =
    condition match {
      case Lt(upper) => ConditionNew("Lt", upper, incl = false)
      case Lte(upper) => ConditionNew("Lte", upper, incl = false)
      case Gt(lower) => ConditionNew("Gt", lower, incl = false)
      case Gte(lower) => ConditionNew("Gte", lower, incl = false)
      case Is(exactValue) => ConditionNew("Is", exactValue, incl = false)
      case Between(lower, upper, true) => ConditionNew("Between", List(lower, upper), incl = true)
      case Between(lower, upper, false) => ConditionNew("Between", List(lower, upper), incl = false)
      case In(elements) => ConditionNew("In", elements, incl = false)
    }

  def getActualValuePrintable(condition: ConditionNew with Serializable): String = {
    if (condition.name == "Between") {
      val condList = condition.value.asInstanceOf[List[Any]]
      condList(0) + " and " + condList(1)
    } else if  (condition.name == "In"){
      val condList = condition.value.asInstanceOf[List[Any]]
      condList.mkString("[",",","]")
    } else {
      condition.value.toString
    }
  }

  def getAssertion(base64String: String) = {
    // WARN: don't believe IntelliJ here, this import is absolutely mandatory, see
    import io.gatling.commons.stats.assertion.AssertionPicklers._
    val bytes = Base64.getDecoder.decode(base64String)
    Unpickle[Assertion].fromBytes(ByteBuffer.wrap(bytes))
  }

  def getAssertionPrintable(assertion: Assertion): String = {
    val condition = getCondition(assertion.condition)
    val actualValue = getActualValuePrintable(condition)
    assertion.path.printable + " : " + assertion.target.printable + " " + assertion.condition.printable + " " + actualValue
  }

  def parseConditionValue(condition: ConditionNew): String = {
    if (condition.name == "Between" || condition.name == "In") {
      val condList = condition.value.asInstanceOf[List[Any]]
      condList.mkString("[",",","]")
    } else {
      condition.value.toString
    }
  }

  def decryptAssertion(assertion: Assertion): String = {
    val path = getPath(assertion.path)
    val target = getTarget(assertion)
    val condition = getCondition(assertion.condition)
    val value = parseConditionValue(condition)
    path._1 + "|" + path._2 + "|" + target._1 + "|" + target._2 + "|" + target._3 + "|" + condition.name + "|" + value + "|" + condition.incl
  }

  def decode (assertion: String): String = {
    val a = getAssertion(assertion)
    val assertionPrintable = getAssertionPrintable(a)
    val assertionDecrypt = decryptAssertion(a)
    assertionDecrypt + "::" + assertionPrintable
  }

  // Main Method
  def main(args: Array[String]) {
    var i: Int = 0;
    if (args.length != 1) {
      throw new Exception("Wrong number of arguments. this program takes only one argument: <encoded_assertion_string>")
    }
    val assertion = getAssertion(args(0))
    val assertionPrintable = getAssertionPrintable(assertion)
    val assertionDecrypt = decryptAssertion(assertion)
    print(assertionDecrypt + "::" + assertionPrintable)
  }

}
