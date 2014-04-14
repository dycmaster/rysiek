package dycmaster.rysiek.triggers.criteriaHandlers;

/*
 * This is so far for absoluteTimeCriteria.
 * You have to:
 * 1. create and give criteria
 * 2. start
 * 3. Receive created trigger and start observing it
 */

import dycmaster.rysiek.triggers.SensorTrigger;
import dycmaster.rysiek.triggers.Trigger;
import dycmaster.rysiek.triggers.triggerParsers.CompoundTriggerLogicalCriterion;
import org.joda.time.DateTime;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

public abstract class AbstractCriteriaHandler {

    private static int triggersCounter = 0;
    protected CompoundTriggerLogicalCriterion _criterion;
    protected Trigger _triggerToObserve;
    protected Timer _timer = null;
    protected boolean initState;

    public AbstractCriteriaHandler(CompoundTriggerLogicalCriterion criterion) {
        _criterion = criterion;
    }

    protected static int getCreatedTriggersCounter() {
        return triggersCounter;
    }

    protected abstract void start();

    public void startHandling(){
        _triggerToObserve = createTriggerToObserve();
        initState = getInitState(new DateTime(_criterion.getTime()));
        _triggerToObserve.setTriggerState(initState);

        start();
    }


    public Trigger getTriggerToObserve() {
        return _triggerToObserve;
    }

    protected  Trigger createTriggerToObserve(){
        return getRawTrigger();
    }

    protected  String getTriggerDesc(){
        return getClass().getSimpleName()+"_no_" + getThisSortHandlersCounter();
    }

    protected abstract int getThisSortHandlersCounter();

    protected abstract  boolean getInitState(DateTime turnOffTime);

    protected Trigger getRawTrigger() {
        triggersCounter++;
        final Trigger rawTrigger = new SensorTrigger();
        rawTrigger.setName("rawTrigger_" + getTriggerDesc() + "_total" + getCreatedTriggersCounter());
        rawTrigger.start();
        return rawTrigger;
    }


}
