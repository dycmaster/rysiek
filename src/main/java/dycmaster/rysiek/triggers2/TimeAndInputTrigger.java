package dycmaster.rysiek.triggers2;

import dycmaster.rysiek.shared.Create;
import org.joda.time.Duration;

import java.util.Map;

public class TimeAndInputTrigger extends  AbstractTrigger {

    private final Map<Integer,String> declaredInputs;   //number, name
    private Map<String, Boolean> inputStates;   //name, state
    private TimeAndInputsTriggerLogic triggerLogic;

    protected Map<String, Boolean> getInputStates(){
        return  inputStates;
    }

    public TimeAndInputTrigger(String name, Map<Integer, String> declaredInputs) {
        super(name);
        this.declaredInputs = declaredInputs;
        initializeInputs(declaredInputs);
    }

    public void  setTriggerLogic(TimeAndInputsTriggerLogic triggerLogic){
        super.setTriggerLogic(triggerLogic);
        this.triggerLogic = triggerLogic;
    }

    private void initializeInputs(Map<Integer, String> inputsMap) {
        //states indexed via name
        this.inputStates = Create.newMap();
        for(String inputName: inputsMap.values()){
            inputStates.put(inputName, false);
        }
    }

    public void setInputState(String inputName, boolean state) {
        inputStates.put(inputName, state);
        updateTriggerStateBasedOnInputs(inputStates);
    }

    public void setInputState(int inputNumber, boolean state) {
        String key = declaredInputs.get(inputNumber);
        inputStates.put(key, state);
        updateTriggerStateBasedOnInputs(inputStates);
    }

    private void updateTriggerStateBasedOnInputs(Map<String, Boolean> inputStates){
        if(isEnabled() && triggerLogic!=null) {
            boolean newState = triggerLogic.processTriggerInputs(inputStates, declaredInputs, getOutputState());
            updateTriggerStateBasedOnLogic(newState);
        }
    }

    public static class Builder{

        //for trigger
        private final String name;
        private final Map<Integer, String> declaredInputs;

        //for logic
        private String logicType;
        private Duration duration;
        private String trackedInput;
        private String description;

        public Builder(String name, Map<Integer, String> declaredInputs){
            this.name = name;
            this.declaredInputs = declaredInputs;
        }


        public Builder withLogicDescription(String logicDescription){
            this.description = logicDescription;
            return  this;
        }

        public Builder withInputTrackedByLogic(String trackedInput){
            this.trackedInput = trackedInput;
            return  this;
        }

        public Builder withLogicDelayWhenTrackingInput(Duration duration){
            this.duration = duration;
            return  this;
        }

        public Builder withLogicDelayWhenTrackingInput(long duration){
            this.duration = new Duration(duration);
            return  this;
        }

        public Builder withLogicType(String logicType){
            this.logicType = logicType;
            return  this;
        }

        public TimeAndInputTrigger build(){

            TimeAndInputTrigger trigger = new TimeAndInputTrigger(this.name, this.declaredInputs);
            trigger.startTriggering();
            TimeAndInputsTriggerLogic logic;

            TriggerLogics trigLogicEnum = Create.newCollection(TriggerLogics.values()).stream()
                    .filter(x->x.getLogicStringName().equals(logicType)).findFirst().get();

            switch(trigLogicEnum){
                case OnLongerThanTime:
                     logic = new TimeAndInputsTriggerLogic.Builder(this.description, this.logicType)
                             .withDuration(this.duration.getMillis())
                             .withTrackedInput(this.trackedInput)
                             .withTrigger(trigger)
                             .build();
                    break;
                default:
                    throw new RuntimeException("Unknown logic type: "+logicType);
            }

            trigger.setTriggerLogic(logic);
            return  trigger;
        }


    }

}
