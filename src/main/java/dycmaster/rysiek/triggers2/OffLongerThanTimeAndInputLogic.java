package dycmaster.rysiek.triggers2;

import org.joda.time.Duration;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This one goes on when a given trackedInput is on for longer than given time
 */
public class OffLongerThanTimeAndInputLogic extends TimeAndInputsTriggerLogic {

    private final Duration duration;
    private final String trackedInput;

    private Timer trackingTimer = new Timer();
    private TimerTask setOnTask = getTimerTask();
    private boolean timerScheduled = false;

    private TimerTask getTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                updateTriggerState(true);
                timerScheduled = false;
            }
        };
    }

    public OffLongerThanTimeAndInputLogic(String description, TimeAndInputTrigger trigger, Duration duration, String trackedInput) {
        super(description, trigger);
        this.duration = duration;
        this.trackedInput = trackedInput;
        run();
    }

    private void run() {
        initTimer();
    }




    private void initTimer() {
        boolean initTrackedInputState = getTriggerInputStates().get(trackedInput);
        updateTriggerState(false);

        if (!initTrackedInputState) {
            timerScheduled = true;
            trackingTimer.schedule(setOnTask, duration.getMillis());
        }
    }

    private boolean updateTimer(boolean trackedInputState, boolean currentTriggerOutput) {
        boolean stateToReturnNow;

        if (trackedInputState) {
            trackingTimer.cancel();
            timerScheduled = false;
            stateToReturnNow = false;
        } else {
            if (currentTriggerOutput) { //already fine
                timerScheduled = false; //just in case
                trackingTimer.cancel();
                stateToReturnNow = true;
            } else {
                if (timerScheduled) { //we're already waiting
                    stateToReturnNow = false;
                } else {
                    trackingTimer = new Timer();
                    setOnTask = getTimerTask();
                    trackingTimer.schedule(setOnTask, duration.getMillis());
                    timerScheduled = true;
                    stateToReturnNow = false;
                }
            }
        }
        return stateToReturnNow;
    }


    @Override
    public boolean processTriggerInputs(Map<String, Boolean> triggerInputStates,
                                        Map<Integer, String> triggerDeclaredInputs,
                                        boolean currentTriggerOutput) {
        boolean stateOfTrackedInput = triggerInputStates.get(trackedInput);
        return updateTimer(stateOfTrackedInput, currentTriggerOutput);

    }
}
