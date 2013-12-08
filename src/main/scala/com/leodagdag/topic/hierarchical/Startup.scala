package com.leodagdag.topic.hierarchical

import akka.actor.{Props, ActorSystem}

import com.leodagdag.topic.hierarchical.data.{Kepler, Oms}

import com.leodagdag.topic.hierarchical.bus.Streams
import com.typesafe.config.ConfigFactory
import com.leodagdag.topic.hierarchical.consumer.{OmsConsumer, StatisticConsumer,KeplerConsumer}
import com.leodagdag.topic.hierarchical.bus.event.DiffusionEvent


class Startup {
  def start() = {
    val config = ConfigFactory.parseString("akka.loglevel = DEBUG \n akka.actor.debug.lifecycle = on \n akka.actor.debug.autoreceive = on \n akka.actor.debug.receive = on").withFallback(ConfigFactory.defaultOverrides())
    val system = ActorSystem("Streams", config)

    val global = system.actorOf(Props[StatisticConsumer], "my-global")
    val oms = system.actorOf(Props[OmsConsumer], "my-oms")
    val kepler = system.actorOf(Props[KeplerConsumer], "my-kepler")
    Streams.diffusion.subscribe(global, "/diffusion")
    Streams.diffusion.subscribe(oms, "/diffusion/oms")
    Streams.diffusion.subscribe(kepler, "/diffusion/kepler")

    Streams.diffusion.publish(new DiffusionEvent("/diffusion/oms", new Oms(1L, "Trade OMS")))
    Streams.diffusion.publish(new DiffusionEvent("/diffusion/kepler", new Kepler(1L, "Trade KEP")))
    system.shutdown()
  }
}

object Startup {
  def main(args: Array[String]) {
    new Startup().start()
  }

}