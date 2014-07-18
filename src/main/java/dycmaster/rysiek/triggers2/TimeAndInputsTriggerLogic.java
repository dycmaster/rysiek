package dycmaster.rysiek.triggers2;



import dycmaster.rysiek.shared.Create;
import org.joda.time.Duration;

import java.util.Map;

public abstract class TimeAndInputsTriggerLogic extends AbstractTriggerLogic {

    private final TimeAndInputTrigger trigger;


    public TimeAndInputsTriggerLogic(String description, TimeAndInputTrigger trigger) {
        super(description);
        this.trigger = trigger;
    }

    protected TimeAndInputTrigger getTrigger(){
        return  this.trigger;
    }

    protected  void updateTriggerState(boolean newState){
        trigger.updateTriggerStateBasedOnLogic(newState);
    }

    protected Map<String, Boolean> getTriggerInputStates(){
        return  trigger.getInputStates();
    }

    public abstract boolean processTriggerInputs(Map<String, Boolean> triggerInputStates,
                                                 Map<Integer, String> triggerDeclaredInputs,
                                                 boolean currentOutput);

    public static class Builder{
        private String logicType;
        private Duration duration;
        private String trackedInput;
        private String description;
        private TimeAndInputTrigger trigger;

        public Builder(String description, String logicType){
            this.logicType = logicType;
            this.description = description;
        }

        public Builder withDuration(long durationMilliseconds){
            this.duration = new Duration(durationMilliseconds);
            return  this;
        }

        public Builder withTrackedInput(String trackedInput){
            this.trackedInput = trackedInput;
            return  this;
        }

        public Builder withTrigger(TimeAndInputTrigger trigger){
            this.trigger = trigger;
            return this;
        }

        public TimeAndInputsTriggerLogic build(){

            TriggerLogic triggerLogicEnum = Create.newCollection(TriggerLogic.values()).stream()
                    .filter(x->x.getLogicStringName().equals(logicType)).findFirst().get();

            switch(triggerLogicEnum){
                case OnLongerThanTime:
                    return new OnLongerThanTimeAndInputLogic(description, trigger, duration,trackedInput);
                default:
                    throw  new RuntimeException(String.format("Unhandled trigger type encountered! (%s)", logicType));
            }
        }

    }
}
