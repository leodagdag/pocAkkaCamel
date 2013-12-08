package com.leodagdag.topic.hierarchical.consumer

import akka.actor.{ActorLogging, Actor}
import akka.event.LoggingReceive
import com.leodagdag.topic.hierarchical.data.Trade

class OmsConsumer extends Actor with ActorLogging{
  def receive: Actor.Receive = LoggingReceive{
    case msg: Trade =>
  }
}