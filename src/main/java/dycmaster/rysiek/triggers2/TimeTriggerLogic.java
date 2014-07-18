package dycmaster.rysiek.triggers2;


import dycmaster.rysiek.shared.Create;
import org.joda.time.Duration;

public abstract class TimeTriggerLogic extends AbstractTriggerLogic {

    private final TimeOnlyTrigger trigger;

    public TimeTriggerLogic(String description, TimeOnlyTrigger trigger) {
        super(description);
        this.trigger = trigger;
    }

    protected  void updateTriggerState(boolean newState){
        trigger.updateTriggerStateBasedOnLogic(newState);
    }


    protected TimeOnlyTrigger getTrigger(){
        return  this.trigger;
    }

    public static class Builder{

        private final String logicDescription;
        private final TimeOnlyTrigger forThisTrigger;
        private String logicType;
        private String cronString;
        private Duration duration;

        public Builder(String logicDescription, TimeOnlyTrigger forThisTrigger){
            this.logicDescription = logicDescription;
            this.forThisTrigger = forThisTrigger;
        }

        public Builder withType(String logicType){
            this.logicType = logicType;
            return  this;
        }

        public Builder withCronString(String cronString){
            this.cronString = cronString;
            return  this;
        }

        public Builder withDuration(long durationMilliseconds){
            this.duration = new Duration(durationMilliseconds);
            return  this;
        }

        public TimeTriggerLogic build(){

            TriggerLogic trigLogEnum = Create.newCollection(TriggerLogic.values()).stream()
                    .filter(x->x.getLogicStringName().equals(logicType)).findFirst().get();

            switch (trigLogEnum){
                case SingleShotCronTimeLogic:
                    return new SingleShotCronTimeLogic(logicDescription, forThisTrigger, cronString);
                case LongerThanTimeLogic:
                    return  new LongerThanTimeLogic(logicDescription, forThisTrigger, duration);

                default:
                    throw new RuntimeException("Unknown logic type: "+logicType);
            }
        }

    }

}
