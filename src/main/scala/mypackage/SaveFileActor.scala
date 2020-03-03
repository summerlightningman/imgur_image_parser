package mypackage

import java.io.File
import java.net.URL

import akka.actor.{Actor, ActorLogging, Props}
import org.apache.commons.io.FileUtils

class SaveFileActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case address: String =>
      log.info(s"Requesting to $address")
      val url = new URL(address)
      if (url.openConnection().getContentLength == 503) {
        log.info(s"Url $url is empty")
      } else {
        log.info(s"Url $url has an image. Downloading ${url.getPath.tail}")
        FileUtils.copyURLToFile(
          url,
          new File(s"${System.getProperty("user.dir")}/src/main/resources/${url.getPath.tail}")
        )
      }
    case End => sender ! End
  }

  override def unhandled(message: Any): Unit = {
    println(message)
  }
}

object SaveFileActor {
  def props(): Props = {
    Props(new SaveFileActor)
  }
}
