package com.leodagdag.hierarchicalTopic

import akka.actor.{Props, ActorSystem}

import com.leodagdag.hierarchicalTopic.data.{Kepler, Oms}

import com.leodagdag.hierarchicalTopic.event.DiffusionEvent
import com.leodagdag.hierarchicalTopic.bus.Streams._
import com.typesafe.config.ConfigFactory
import com.leodagdag.hierarchicalTopic.consumer.{OmsConsumer, StatisticConsumer,KeplerConsumer}


class Startup {

  def start() = {
    val config = ConfigFactory.parseString("akka.loglevel = DEBUG \n akka.actor.debug.lifecycle = on \n akka.actor.debug.autoreceive = on \n akka.actor.debug.receive = on").withFallback(ConfigFactory.defaultOverrides())
    val system = ActorSystem("Streams", config)

    val global = system.actorOf(Props[StatisticConsumer], "my-global")
    val oms = system.actorOf(Props[OmsConsumer], "my-oms")
    val kepler = system.actorOf(Props[KeplerConsumer], "my-kepler")
    diffusion.subscribe(global, "/diffusion")
    diffusion.subscribe(oms, "/diffusion/oms")
    diffusion.subscribe(kepler, "/diffusion/kepler")

    diffusion.publish(new DiffusionEvent("/diffusion/oms", new Oms(1L, "Trade OMS")))
    diffusion.publish(new DiffusionEvent("/diffusion/kepler", new Kepler(1L, "Trade KEP")))
  }
}

object Startup {
  def main(args: Array[String]) {
    new Startup().start()
  }

}