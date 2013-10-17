package com.mlh.stockman.core

import akka.actor._
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._

class SimpleClusterListener extends Actor with ActorLogging {
  def receive = {
    case state: CurrentClusterState ⇒
      log.debug("Current members: {}", state.members.mkString(", "))
    case MemberUp(member) ⇒
      log.debug("Member is Up: {}", member.address)
    case UnreachableMember(member) ⇒
      log.debug("Member detected as unreachable: {}", member)
    case MemberRemoved(member, previousStatus) ⇒
      log.debug("Member is Removed: {} after {}",
        member.address, previousStatus)
    case _: ClusterDomainEvent ⇒ // ignore
  }
}
