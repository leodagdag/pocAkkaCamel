package com.leodagdag.hierarchicalTopic.consumer

import akka.actor.{ActorLogging, Actor}
import akka.event.LoggingReceive

/**
 * Created by leo on 07/12/13.
 */
class OmsConsumer extends Actor with ActorLogging{
  def receive: Actor.Receive = LoggingReceive{
    case _ =>
  }
}