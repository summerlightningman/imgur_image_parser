package mypackage

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

class MasterActor(system: ActorSystem) extends Actor with ActorLogging {

  val urlGenerators: Seq[ActorRef] =
    (0 to 4).map(i => context.actorOf(GenerateUrlActor.props(), name = s"urlGenerator-$i"))
  var counter = 0

  override def receive: Receive = {
    case Start =>
      urlGenerators.foreach(_ ! Start)
    case End =>
      counter += 1
      if (counter == urlGenerators.length) system.terminate()
  }
}


object MasterActor {
  def props(system: ActorSystem) = {
    Props(new MasterActor(system))
  }
}

