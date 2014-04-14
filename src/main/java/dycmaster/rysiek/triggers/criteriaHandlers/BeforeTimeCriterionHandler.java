package dycmaster.rysiek.triggers.criteriaHandlers;


import dycmaster.rysiek.triggers.Trigger;
import dycmaster.rysiek.triggers.triggerParsers.CompoundTriggerLogicalCriterion;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.Period;

import java.util.Timer;
import java.util.TimerTask;

public class BeforeTimeCriterionHandler extends AbstractCriteriaHandler {

    protected static int beforeTimeTriggerCounter = 0;
    Logger log = Logger.getLogger(BeforeTimeCriterionHandler.class);



    public BeforeTimeCriterionHandler(CompoundTriggerLogicalCriterion criterion) {
        super(criterion);
        beforeTimeTriggerCounter++;
    }

    @Override
    protected void start() {


        DateTime turnOffAt = new DateTime(_criterion.getTime());

        if (initState) {
            scheduleTurnOff(_triggerToObserve, turnOffAt );
        } else {
            scheduleTurnOn(_triggerToObserve, turnOffAt );
        }
    }

    @Override
    protected int getThisSortHandlersCounter() {
        return beforeTimeTriggerCounter;
    }

    @Override
    protected  boolean getInitState(DateTime turnOffTime) {
        DateTime now = new DateTime();
        return DateTimeComparator.getTimeOnlyInstance().compare(turnOffTime, now) >= 0;
    }


    protected void scheduleTurnOn(final Trigger targetTrigger, final DateTime turnOffAtCriteria ) {
        DateTime turnOnAt = new DateTime();
        turnOnAt = turnOnAt.plus(Period.days(1));
        turnOnAt = turnOnAt.secondOfDay().setCopy(1);

        Timer switchOnTimer = new Timer();
        switchOnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.debug("turnOn timer goes off (trigger:"+getTriggerToObserve().getName());
                targetTrigger.setTriggerState(true);
                scheduleTurnOff(targetTrigger, turnOffAtCriteria);
            }
        }, turnOnAt.toDate());
        log.debug("turnOn scheduled (Trigger:"+getTriggerToObserve().getName());

        _timer = switchOnTimer;
    }


    protected void scheduleTurnOff(final Trigger targetTrigger, final  DateTime turnOffAtCriteria ) {

        DateTime turnOffAtToday = new DateTime();

        turnOffAtToday = turnOffAtToday.withTime(
                turnOffAtCriteria.getHourOfDay(), turnOffAtCriteria.getMinuteOfHour(), turnOffAtCriteria.getSecondOfMinute(), turnOffAtCriteria.getMillisOfSecond());

        Timer switchOffTimer = new Timer();
        switchOffTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.debug("turnOff timer goes off (trigger:"+getTriggerToObserve().getName());
                targetTrigger.setTriggerState(false);
                scheduleTurnOn(targetTrigger, turnOffAtCriteria);
            }
        }, turnOffAtToday.toDate());
        log.debug("turnOff scheduled (Trigger:"+getTriggerToObserve().getName());

        _timer = switchOffTimer;
    }

}
