package com.leodagdag.flatTopic

import com.typesafe.config.ConfigFactory
import akka.actor.{Props, ActorSystem}
import com.leodagdag.flatTopic.consumer.{KeplerConsumer, OmsConsumer, GlobalConsumer}
import com.leodagdag.flatTopic.bus.event.DiffusionEvent

import com.leodagdag.flatTopic.data.Kepler
import com.leodagdag.flatTopic.bus.Streams
import com.leodagdag.flatTopic.data.Oms


class Startup {

  def start() = {
    val config = ConfigFactory.parseString("akka.loglevel = DEBUG \n akka.actor.debug.lifecycle = on \n akka.actor.debug.autoreceive = on \n akka.actor.debug.receive = on").withFallback(ConfigFactory.defaultOverrides())
    val system = ActorSystem("Streams", config)

    val global = system.actorOf(Props[GlobalConsumer], "my-global")
    val oms = system.actorOf(Props[OmsConsumer], "my-oms")
    val kepler = system.actorOf(Props[KeplerConsumer], "my-kepler")
    Streams.diffusion.subscribe(global, "/diffusion")
    Streams.diffusion.subscribe(oms, "/diffusion/oms")
    Streams.diffusion.subscribe(kepler, "/diffusion/kepler")

    Streams.diffusion.publish(new DiffusionEvent("/diffusion/oms", new Oms(1, "Trade OMS")))
    Streams.diffusion.publish(new DiffusionEvent("/diffusion/kepler", new Kepler(2, "Trade KEP")))
  }


}

object Startup {
  def main(args: Array[String]) {
    new Startup().start()
  }

}
