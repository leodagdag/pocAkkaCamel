package com.leodagdag.flatTopic.consumer

import akka.actor.{ActorLogging, Actor}
import akka.event.LoggingReceive


class GlobalConsumer extends Actor with ActorLogging{
  def receive: Actor.Receive = LoggingReceive{
    case _ =>
  }
}
