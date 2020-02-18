package com.github.marufaytekin

import com.github.marufaytekin.Decoder._
import org.scalatest.FlatSpec

class DecoderSpec extends FlatSpec{

  val assertion = getAssertion ("AAMAAQhyZXF1ZXN0MQQGAAAAAAAAAPA/AAAAAAAANEAB")
  val assertionText = "AAMAAQhyZXF1ZXN0MQQGAAAAAAAAAPA/AAAAAAAANEAB"

  it should "produce request1 : mean requests per second is between inclusive 1.0 and 20.0" in {
    assert(getAssertionPrintable(assertion) == "request1 : mean requests per second is between inclusive 1.0 and 20.0")
  }

  it should "produce Details|request1|MeanRequestsPerSecondTarget|null|null|Between|[1.0,20.0]|true" in {
    assert(decryptAssertion(assertion) == "Details|request1|MeanRequestsPerSecondTarget|null|null|Between|[1.0,20.0]|true")
  }

  it should "return Details|request1|MeanRequestsPerSecondTarget|null|null|Between|[1.0,20.0]|true::request1 : mean requests per second is between inclusive 1.0 and 20.0" in {
    assert(decode(assertionText) == "Details|request1|MeanRequestsPerSecondTarget|null|null|Between|[1.0,20.0]|true::request1 : mean requests per second is between inclusive 1.0 and 20.0")
  }

}
