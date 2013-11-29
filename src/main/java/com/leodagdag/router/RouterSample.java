package com.leodagdag.router;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.FromConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * @author leo
 */
public class RouterSample {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("RouterSample", ConfigFactory.load());

        ActorRef warmup = system.actorOf(Props.create(WarmUp.class), "my-warmup");
        for (int i = 0; i < 10; i++) {
            warmup.tell(i, null);
        }
    }


    public static class WarmUp extends UntypedActor {
        private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
        private final ActorRef intermediate;

        public WarmUp() {
            intermediate = getContext().actorOf(Props.create(Intermediate.class),"my-intermediate"
            );
        }

        /**
         * To be implemented by concrete UntypedActor, this defines the behavior of the
         * UntypedActor.
         */
        @Override
        public void onReceive(Object message) throws Exception {
            log.info("receive message {} - Thread {}", message, Thread.currentThread().toString());
            intermediate.forward(message, getContext());
        }
    }

    public static class Intermediate extends UntypedActor {
        private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
        private final ActorRef router;

        public Intermediate() {
            router = getContext().actorOf(Props.create(Worker.class).withRouter(FromConfig.getInstance())/*.withDispatcher("my-dispatcher")*/,
                    "router-worker"
            );
        }

        /**
         * To be implemented by concrete UntypedActor, this defines the behavior of the
         * UntypedActor.
         */
        @Override
        public void onReceive(Object message) throws Exception {
            log.info("receive message {} - Thread {}", message, Thread.currentThread().toString());
            router.forward(message, getContext());
        }
    }

    public static class Worker extends UntypedActor {
        private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

        /**
         * To be implemented by concrete UntypedActor, this defines the behavior of the
         * UntypedActor.
         */
        @Override
        public void onReceive(Object message) throws Exception {
            log.info("receive message {} - Thread {}", message, Thread.currentThread().toString());

        }
    }
}
