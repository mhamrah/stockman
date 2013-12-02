package com.mlh.stockman.api

import akka.actor.{ ActorRefFactory, ActorRef }
import scala.concurrent.ExecutionContext
import spray.routing.Directives
import spray.can.server.Stats
import spray.httpx.marshalling.Marshaller
import scala.concurrent.duration._
import spray.http._
import spray.util._
import akka.pattern.ask
import spray.can.Http

class StatsRoute(actorRefFactory: ActorRefFactory)(implicit executionContext: ExecutionContext)
    extends Directives {

  val route =
    path("stats") {
      complete {
        actorRefFactory.actorSelection("/user/IO-HTTP/listener-0")
          .ask(Http.GetStats)(1.second)
          .mapTo[Stats]
      }
    }

  implicit val statsMarshaller: Marshaller[Stats] =
    Marshaller.delegate[Stats, String](ContentTypes.`text/plain`) { stats =>
      "Uptime                : " + stats.uptime.formatHMS + '\n' +
      "Total requests        : " + stats.totalRequests + '\n' +
      "Open requests         : " + stats.openRequests + '\n' +
      "Max open requests     : " + stats.maxOpenRequests + '\n' + 2
      "Total connections     : " + stats.totalConnections + '\n' +
      "Open connections      : " + stats.openConnections + '\n' +
      "Max open connections  : " + stats.maxOpenConnections + '\n' +
      "Requests timed out    : " + stats.requestTimeouts + '\n'
    }
}
