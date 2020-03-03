package mypackage

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import scala.util.Random

class GenerateUrlActor extends Actor with ActorLogging {
  val alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
  val random = new Random()
  var counter = 0
  val countOfSavers = 4
  val savers: Seq[ActorRef] = (1 to countOfSavers).map(i => context.actorOf(SaveFileActor.props(), name = s"Saver-$i"))

  override def receive: Receive = {
    case Start =>
      for (i <- 0 to 100) {
        val url: String = "https://i.imgur.com/" +
          (0 to 4).map(_ => alphabet(random.between(0, alphabet.length))).mkString("") +
          ".jpg"
        val index = i % countOfSavers
        log.info(s"Iteration: $i")
        log.info(s"Sending $url to Saver-$index")
        savers.apply(index) ! url
      }
      savers.foreach(_ ! End)
    case End =>
      counter += 1
      log.info(s"End by $sender. Total: $counter")
      if (counter == savers.length) context.parent ! End
  }
}

object GenerateUrlActor {
  def props(): Props = {
    Props(new GenerateUrlActor)
  }
}
