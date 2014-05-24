package com.mlh.stockman.directives

import org.scalatest._
import org.scalatest.matchers._
import spray.testkit.ScalatestRouteTest
import spray.http._
import spray.routing.Directives._

class UuidDirectivesSpec
  extends FreeSpec
  with Matchers
  with UuidDirectives
  with ScalatestRouteTest {

      val uuidRoute =  generateUuid { uuid => complete(uuid.toString) }
  "The UUID Directive" - {
    "will generate different UUID per request" in {

      var uuid1: String = ""
      var uuid2: String = ""
      Get() ~> uuidRoute ~> check  {
        responseAs[String].size shouldBe 36
        uuid1 = responseAs[String]
      }
      Get() ~> uuidRoute ~> check  {
        responseAs[String].size shouldBe 36
        uuid2 = responseAs[String]
      }
      uuid1 shouldNot equal (uuid2)
    }
    "can extract a uuid value from the header" in {
      val uuid = java.util.UUID.randomUUID.toString

      Get() ~> addHeader("correlation-id", uuid) ~> uuidRoute ~> check {
        responseAs[String] shouldEqual uuid
      }
    }
    "can extract the same uuid twice" in {
      var uuid1: String =""
      var uuid2: String = ""
      Get() ~> generateUuid { uuid =>
        {
          uuid1 = uuid.toString
          generateUuid { another =>
            uuid2 = another.toString
            complete("")
          }
        }
      } ~> check {
        uuid1 shouldEqual uuid2
      }
    }

  }
}

