package com.leodagdag.cache

import akka.actor.{ActorLogging, Actor, Props, ActorSystem}
import com.leodagdag.cache.database.Database
import com.typesafe.config.ConfigFactory
import com.leodagdag.cache.database.Database.Request
import akka.event.LoggingReceive

class Startup {
  def start() = {
    val config = ConfigFactory.parseString("akka.loglevel = DEBUG \n akka.actor.debug.lifecycle = on \n akka.actor.debug.autoreceive = on \n akka.actor.debug.receive = on").withFallback(ConfigFactory.defaultOverrides())
    val system = ActorSystem("Cache", config)
    system.actorOf(Props[Main], "my-main")

  }
}

object Startup {
  def main(args: Array[String]) {
    new Startup().start()
  }
}

class Main extends Actor with ActorLogging{
  val database = context.actorOf(Props[Database], "my-database")
  database ! Request(self, "1")
  database ! Request(self, "15")
  database ! Request(self, "15")

  def receive: Actor.Receive = LoggingReceive{
    case _ =>
  }
}