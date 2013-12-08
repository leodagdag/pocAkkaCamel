package com.leodagdag.topic.flat.consumer

import akka.actor.{ActorLogging, Actor}
import akka.event.LoggingReceive
import com.leodagdag.topic.flat.data.Trade


class OmsConsumer extends Actor with ActorLogging{
  def receive: Actor.Receive = LoggingReceive{
    case msg: Trade =>
  }
}