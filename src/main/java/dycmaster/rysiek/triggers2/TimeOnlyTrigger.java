package dycmaster.rysiek.triggers2;


import dycmaster.rysiek.shared.Create;
import org.joda.time.Duration;

public class TimeOnlyTrigger extends AbstractTrigger {

    public TimeOnlyTrigger(String name) {
        super(name);
    }

    public void setTriggerLogic(TimeTriggerLogic triggerLogic){
        super.setTriggerLogic(triggerLogic);
    }

    public static class Builder{

        //for trigger
        private String name;

        //for logics
        private  String logicDescription;
        private String logicType;
        private String cronString;
        private Duration duration;

        public Builder withLogicDuration(long logicDuration){
            this.duration = new Duration(logicDuration);
            return this;
        }

        public Builder withLogicCronString(String cronString){
            this.cronString = cronString;
            return  this;
        }

        public Builder withLogicType(String logicType){
            this.logicType = logicType;
            return  this;
        }

        public Builder withLogicDescription(String logicDescription){
            this.logicDescription = logicDescription;
            return this;
        }


        public Builder(String triggerName){
            this.name = triggerName;
        }

        public TimeOnlyTrigger build(){
            TimeOnlyTrigger trigger = new TimeOnlyTrigger(this.name);
            trigger.startTriggering();
            TimeTriggerLogic logic;

            TriggerLogics trigLogicEnum = Create.newCollection(TriggerLogics.values()).stream()
                    .filter(x->x.getLogicStringName().equals(logicType)).findFirst().get();

            switch (trigLogicEnum){
                case SingleShotCronTimeLogic:
                    logic = new SingleShotCronTimeLogic(this.logicDescription, trigger, cronString);
                    break;

                case LongerThanTimeLogic:
                    logic = new LongerThanTimeLogic(logicDescription, trigger, duration);
                    break;

                default:
                    throw new RuntimeException("Unknown logic type for the trigger! Logic type " +
                            "requested was: " + logicType);
            }
            trigger.setTriggerLogic(logic);
            return  trigger;
        }

    }

}
