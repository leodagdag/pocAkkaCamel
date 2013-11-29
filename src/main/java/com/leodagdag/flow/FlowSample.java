package com.leodagdag.flow;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;

import java.util.Map;

import static com.leodagdag.flow.Worker.WorkerApi.*;


/**
 * @author leo
 */
public class FlowSample {

    public static void main(String[] args) {

        Config config = ConfigFactory.parseString("akka.loglevel = DEBUG \n akka.log-config-on-start = off \n akka.actor.debug.receive = off \n akka.actor.debug.autoreceive=off").withFallback(ConfigFactory.defaultReference());

        ActorSystem system = ActorSystem.create("FlowSample", config);
        ActorRef worker = system.actorOf(Props.create(Worker.class), "my-worker");
        Config actorDebugConfig = config.getConfig("akka.actor.debug");
        for(Map.Entry<String, ConfigValue> key : actorDebugConfig.entrySet()){
            System.out.println("= [" + key.getKey() + "=" + key.getValue() +"]");
        }
        System.out.println("akka.actor.debug.receive=" + config.getString("akka.actor.debug.receive"));
        System.out.println("akka.actor.debug.autoreceive=" + config.getString("akka.actor.debug.autoreceive"));
        System.out.println("akka.actor.debug.lifecycle=" + config.getString("akka.actor.debug.lifecycle"));
        worker.tell(Start, null);
    }
}
