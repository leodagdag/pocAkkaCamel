package com.leodagdag.flow;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.Option;

import java.util.ArrayList;
import java.util.List;

import static com.leodagdag.flow.Risk.RiskApi.*;

/**
 * @author leo
 */
public class Risk extends UntypedActor {

    final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public static class RiskApi {

        public static class Data {
            public final String value;

            public Data(final String value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return String.format("%s(%s)", getClass().getSimpleName(), value);
            }
        }

        public static class CheckedException extends RuntimeException {
            private static final long serialVersionUID = 1L;

            public CheckedException(String msg) {
                super(msg);
            }
        }
        public static class UnCheckedException extends RuntimeException {
            private static final long serialVersionUID = 1L;
            public final int index;
            public UnCheckedException(String msg, int index) {
                super(msg);
                this.index = index;
            }


        }
    }

    private final List<Integer> db = new ArrayList<>();

    /**
     * User overridable callback: '''By default it disposes of all children and then calls `postStop()`.'''
     * <p/>
     * Is called on a crashed Actor right BEFORE it is restarted to allow clean
     * up of resources before Actor is terminated.
     */
    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        getSelf().tell(message.get(), getSelf());
    }


    /**
     * To be implemented by concrete UntypedActor, this defines the behavior of the
     * UntypedActor.
     */
    @Override
    public void onReceive(Object message) {
        log.debug("message received: {}", message);
        if (message instanceof Integer) {
            if(message == 3 && db.size() != 0){
                throw new CheckedException("CheckedException");
            }
            if (db.size() != 0 && db.size() % 10 == 0) {
                throw new UnCheckedException("A RuntimeException occurred", (Integer) message);
            }
            db.add((Integer) message);
            log.debug("Insert message {} into database {}", message, db);
        } else unhandled(message);
    }

}
