package com.leodagdag.flow;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Function;
import scala.concurrent.duration.Duration;


import java.util.concurrent.TimeUnit;

import static akka.actor.SupervisorStrategy.Directive;
import static akka.actor.SupervisorStrategy.resume;
import static akka.actor.SupervisorStrategy.restart;
import static akka.actor.SupervisorStrategy.stop;
import static akka.actor.SupervisorStrategy.escalate;
import static com.leodagdag.flow.Risk.RiskApi.CheckedException;
import static com.leodagdag.flow.Worker.WorkerApi.*;


/**
 * @author leo
 */
public class Worker extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public static class WorkerApi {
        public static final Object Start = "Start";
        public static final Object Do = "Do";
    }

    /**
     * User overridable callback.
     * <p/>
     * Is called when an Actor is started.
     * Actor are automatically started asynchronously when created.
     * Empty default implementation.
     */
    @Override
    public void preStart() throws Exception {
        getContext().system().eventStream().subscribe(getSelf(), DeadLetter.class);

    }

    final ActorRef risk = getContext().actorOf(Props.create(Risk.class), "my-risk");
    final ActorRef myRescue = getContext().actorOf(Props.create(Rescue.class), "my-rescue");
    private int index = 0;

    /**
     * To be implemented by concrete UntypedActor, this defines the behavior of the
     * UntypedActor.
     */
    @Override
    public void onReceive(Object message) throws Exception {
        //log.debug("message received: {}", message);
        if (Start.equals(message)) {
            getContext().system().scheduler().schedule(
                    Duration.Zero(),
                    Duration.create(50, "millisecond"),
                    getSelf(),
                    Do,
                    getContext().dispatcher(),
                    null
            );
        } else if (Do.equals(message)) {
            risk.tell(++index, getSelf());
        } else if (message instanceof DeadLetter) {
            myRescue.forward(message, getContext());
        } else {
            unhandled(message);
        }
    }

    /**
     * User overridable definition the strategy to use for supervising
     * child actors.
     */
    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(-1,
                Duration.Inf(), new Function<Throwable, Directive>() {
            @Override
            public Directive apply(Throwable t) throws Exception {
                if (t instanceof CheckedException) {
                    return restart();
                } else if (t instanceof Risk.RiskApi.UnCheckedException) {
                    getContext().system().deadLetters().forward(((Risk.RiskApi.UnCheckedException) t).index, getContext());
                    return stop();
                } else {
                    return stop();
                }
            }
        });
    }
}
