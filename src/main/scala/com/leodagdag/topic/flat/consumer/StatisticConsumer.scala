package com.leodagdag.topic.flat.consumer

import akka.actor.{ActorLogging, Actor}
import akka.event.LoggingReceive
import com.leodagdag.topic.flat.data.Trade


class StatisticConsumer extends Actor with ActorLogging{
  private var nb:Int = 0
  def receive: Actor.Receive = LoggingReceive{
    case msg: Trade =>
      nb += 1
      log.info("receive {} => total {}", msg, nb)
  }
}
