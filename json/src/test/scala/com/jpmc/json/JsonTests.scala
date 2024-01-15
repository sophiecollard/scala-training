package com.jpmc.json

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class JsonTests extends AnyFunSuite with ScalaCheckPropertyChecks with Matchers {
  import JsonTests._

  test("The printJson method should always be able to return a String") {
    forAll { json: Json =>
      Json.printJson(json).getClass.getName shouldBe "java.lang.String"
    }
  }

  test("The dropNulls method should remove all null values") {
    forAll { json: Json =>
      Json.dropNulls(json).map(Json.printJson).exists(_.contains("null")) shouldBe false
    }
  }
}

object JsonTests {

  private val nullGen: Gen[Json] =
    Gen.const(Json.Null)

  private val booleanGen: Gen[Json] =
    Gen.oneOf(List(Json.Boolean(true), Json.Boolean(false)))

  private val intGen: Gen[Json] =
    Gen.chooseNum(minT = Int.MinValue, maxT = Int.MaxValue).map(Json.Int)

  private val stringGen: Gen[Json] =
    Gen.alphaNumStr.map(Json.String)

  private lazy val arrayGen: Gen[Json] =
    Gen.listOf(jsonGen).map(Json.Array)

  private lazy val kvGen: Gen[(String, Json)] =
    for {
      key <- Gen.alphaNumStr
      value <- jsonGen
    } yield (key, value)

  private lazy val objectGen: Gen[Json] =
    Gen.mapOf(kvGen).map(Json.Object)

  private lazy val jsonGen: Gen[Json] =
    Gen.oneOf(nullGen, booleanGen, intGen, stringGen, arrayGen, objectGen)

  implicit lazy val arb: Arbitrary[Json] =
    Arbitrary(jsonGen)

}
