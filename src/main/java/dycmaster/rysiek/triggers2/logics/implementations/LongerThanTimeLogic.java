package dycmaster.rysiek.triggers2.logics.implementations;

import dycmaster.rysiek.triggers2.TimeOnlyTrigger;
import dycmaster.rysiek.triggers2.TimeTriggerLogic;
import org.joda.time.Duration;

import java.util.Timer;
import java.util.TimerTask;


/**
 * This one is fun :) Simply, after a given time since it's been created - it sets the
 * trigger into state '1'. Can be run only once.
 */
public class LongerThanTimeLogic extends TimeTriggerLogic {

    private final Duration duration;

    public LongerThanTimeLogic(String description, TimeOnlyTrigger trigger, Duration duration) {
        super(description, trigger);
        this.duration = duration;
        run();
    }


    private void run(){

        if(duration.getMillis()<=0){
            updateTriggerState(true);
        }else{
            TimerTask setTriggerOn = new TimerTask() {
                @Override
                public void run() {
                    updateTriggerState(true);
                }
            };
            updateTriggerState(false);
            Timer timer = new Timer();
            timer.schedule(setTriggerOn, duration.getMillis());
        }
    }

    
}
