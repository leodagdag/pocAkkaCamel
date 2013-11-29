package com.leodagdag.flow;

import akka.actor.DeadLetter;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.Option;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leo
 */
public class Rescue extends UntypedActor {

    final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public static class RescueApi {

        public static class Data {
            public final Object content;

            public Data(final Object content) {
                this.content = content;
            }

            @Override
            public String toString() {
                return String.format("%s(%s)", getClass().getSimpleName(), content);
            }
        }
    }

    private static final List<Object> queue = new ArrayList<>();

    /**
     * To be implemented by concrete UntypedActor, this defines the behavior of the
     * UntypedActor.
     */
    @Override
    public void onReceive(Object message) throws Exception {
        log.debug("message receive {}", message);
        if (message instanceof DeadLetter) {
            log.info("rescue message {} from {}", message, getSender());
            queue.add( ((DeadLetter) message).message());
            log.info("rescue queue content {}", queue);
        } else unhandled(message);
    }
}
