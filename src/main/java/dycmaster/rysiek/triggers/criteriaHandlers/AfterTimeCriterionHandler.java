package dycmaster.rysiek.triggers.criteriaHandlers;


import dycmaster.rysiek.triggers.Trigger;
import dycmaster.rysiek.triggers.triggerParsers.CompoundTriggerLogicalCriterion;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.LocalTime;
import org.joda.time.Period;

import java.util.Timer;
import java.util.TimerTask;

public class AfterTimeCriterionHandler extends AbstractCriteriaHandler {

    Logger log = Logger.getLogger(AfterTimeCriterionHandler.class);
    private static int afterTimeTriggerCounter = 0;


    public AfterTimeCriterionHandler(CompoundTriggerLogicalCriterion criterion) {
        super(criterion);
        afterTimeTriggerCounter++;
    }

    @Override
    protected void start() {

        LocalTime turnOnTimeToday = new LocalTime(_criterion.getTime());
        LocalTime turnOffTimeTomorrow = new LocalTime(0,0,1);

        if (initState) {
            scheduleTurnOff(_triggerToObserve, turnOffTimeTomorrow, turnOnTimeToday );
        } else {
            scheduleTurnOn(_triggerToObserve, turnOffTimeTomorrow, turnOnTimeToday  );
        }

    }

    protected void scheduleTurnOn(final Trigger targetTrigger,
                                  final LocalTime turnOffAtTime, final LocalTime turnOnAtTime ) {

        DateTime turnOnAtToday = new DateTime();
        turnOnAtToday = turnOnAtToday.withTime(turnOnAtTime.getHourOfDay(), turnOnAtTime.getMinuteOfHour(), turnOnAtTime.getSecondOfMinute(), turnOnAtTime.getMillisOfSecond());


        Timer switchOnTimer = new Timer();
        switchOnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.debug("turnOn timer goes off (trigger:"+getTriggerToObserve().getName());
                targetTrigger.setTriggerState(true);
                scheduleTurnOff(targetTrigger, turnOffAtTime, turnOnAtTime);
            }
        }, turnOnAtToday.toDate());
        log.debug("turnOn scheduled (Trigger:"+getTriggerToObserve().getName());

        _timer = switchOnTimer;
    }


    /*
     * This is called when the trigger is still On. I want to switch it off 1s after the
     * midnight.
     */
    protected void scheduleTurnOff(final Trigger targetTrigger,
                                   final  LocalTime turnOffAtTime, final LocalTime turnOnAtTime ) {

        DateTime turnOffAtTomorrow = new DateTime();
        turnOffAtTomorrow  = turnOffAtTomorrow.plus(Period.days(1));
        //today but with a time given
        turnOffAtTomorrow = turnOffAtTomorrow.withTime(
                turnOffAtTime.getHourOfDay(), turnOffAtTime.getMinuteOfHour(), turnOffAtTime.getSecondOfMinute(), turnOffAtTime.getMillisOfSecond());

        Timer switchOffTimer = new Timer();
        switchOffTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.debug("turnOff timer goes off (trigger:"+getTriggerToObserve().getName());
                targetTrigger.setTriggerState(false);
                scheduleTurnOn(targetTrigger, turnOffAtTime, turnOnAtTime);
            }
        }, turnOffAtTomorrow.toDate());
        log.debug("turnOff scheduled (Trigger:"+getTriggerToObserve().getName());

        _timer = switchOffTimer;
    }

    @Override
    protected boolean getInitState(DateTime turnOnTime) {
        DateTime now = new DateTime();
        boolean state =  DateTimeComparator.getTimeOnlyInstance().compare(turnOnTime, now) < 0;
        log.debug("Trigger init state:"+state);
        return  state;
    }


    @Override
    protected int getThisSortHandlersCounter() {
        return afterTimeTriggerCounter;
    }

}
