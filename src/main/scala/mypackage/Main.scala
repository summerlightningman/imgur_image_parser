package mypackage

import akka.actor.ActorSystem

object Main extends App {
  val system: ActorSystem = ActorSystem("imgur")
  val master = system.actorOf(MasterActor.props(system), name = "Master")
  master ! Start
}
