package com.leodagdag.topic.hierarchical.bus

import akka.event.{ActorEventBus, SubchannelClassification}
import akka.util.Subclassification
import com.leodagdag.topic.hierarchical.bus.event.DiffusionEvent

class DiffusionEventBus extends ActorEventBus with SubchannelClassification {

  type Event = DiffusionEvent
  type Classifier = String

  protected def classify(event: Event): Classifier = event.channel

  protected def subclassification = new Subclassification[Classifier] {
    def isEqual(x: Classifier, y: Classifier) = x == y

    def isSubclass(x: Classifier, y: Classifier) = x.startsWith(y)
  }

  protected def publish(event: Event, subscriber: Subscriber): Unit = {
    subscriber ! event.message
  }

  override def subscribe(subscriber: DiffusionEventBus#Subscriber, to: DiffusionEventBus#Classifier): Boolean = {
    println(s"$subscriber subscribe to $to")
    super.subscribe(subscriber, to)
  }
}
