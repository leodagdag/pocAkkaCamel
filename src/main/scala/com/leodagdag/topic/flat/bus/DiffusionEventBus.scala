package com.leodagdag.topic.flat.bus

import akka.event.{LookupClassification, ActorEventBus}
import com.leodagdag.topic.flat.bus.event.DiffusionEvent


class DiffusionEventBus extends ActorEventBus with LookupClassification {

  type Event = DiffusionEvent
  type Classifier = String

  protected def mapSize(): Int = 2

  protected def classify(event: Event): Classifier = event.channel

  protected def publish(event: Event, subscriber: Subscriber): Unit = {
    subscriber ! event.message
  }

  override def subscribe(subscriber: DiffusionEventBus#Subscriber, to: DiffusionEventBus#Classifier): Boolean = {
    println(s"$subscriber subscribe to $to")
    super.subscribe(subscriber, to)
  }
}
