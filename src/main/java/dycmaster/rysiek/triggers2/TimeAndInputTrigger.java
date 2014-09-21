package dycmaster.rysiek.triggers2;

import dycmaster.rysiek.shared.Create;
import org.joda.time.Duration;

import java.util.Map;

public class TimeAndInputTrigger extends  AbstractTrigger implements IInputTrigger {

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

    @Override
    public void setInputState(String inputName, boolean state) {
        inputStates.put(inputName, state);
        updateTriggerStateBasedOnInputs(inputStates);
    }

    @Override
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
        private  String name;
        private  Map<Integer, String> declaredInputs;

        //for logic
        private String logicType;
        private Duration duration;
        private String trackedInput;
        private String description;

        public Builder(){}


        public Builder(String name, Map<Integer, String> declaredInputs){
            this.name = name;
            this.declaredInputs = declaredInputs;
        }

        public Builder withName(String name){
            this.name = name;
            return  this;
        }

        public Builder withDeclaredInputs(Map<Integer, String> declaredInputs){
            this.declaredInputs = declaredInputs;
            return  this;
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

        public Builder fromSingleString(String declarationString, String separator){
            String [] values = declarationString.split(separator);
            String tName = values[0];

            //inputs
            Integer inputCount = Integer.valueOf(values[2]);
            String [] inputNames = new String[inputCount];
            for(int i=0; i<inputCount; i++){
                inputNames[i] = values[3+i];
            }
            Map<Integer, String> declaredInputs = Create.newMap();
            int c=0;
            for(String str: inputNames){
                declaredInputs.put(++c, str);
            }

            //logic
            String logicType = values[2+1+inputCount];
            String logicDescription = values[2+1+inputCount+1];
            TriggerLogic logic = TriggerLogic.getByParserName(logicType);

            //const for all
            this.withName(tName)
                    .withDeclaredInputs(declaredInputs)
                    .withLogicDescription(logicDescription)
                    .withLogicType(logic.getLogicStringName());

            switch (logic) {
                case OnLongerThanTime:
                    String trackedInput = values[2+1+inputCount+1+1];
                    String duration = values[2+1+inputCount+1+1+1];
                    return this.withInputTrackedByLogic(trackedInput)
                            .withLogicDelayWhenTrackingInput(Long.parseLong(duration));

                case OffLongerThanTime:
                    trackedInput = values[2+1+inputCount+1+1];
                    duration = values[2+1+inputCount+1+1+1];
                    return this.withInputTrackedByLogic(trackedInput)
                            .withLogicDelayWhenTrackingInput(Long.parseLong(duration));

                default:
                    throw new RuntimeException("Unknown logic type " + logicType);
            }
        }

        public TimeAndInputTrigger build(){

            TimeAndInputTrigger trigger = new TimeAndInputTrigger(this.name, this.declaredInputs);
            trigger.startTriggering();
            TimeAndInputsTriggerLogic logic;

            TriggerLogic trigLogicEnum = Create.newCollection(TriggerLogic.values()).stream()
                    .filter(x->x.getLogicStringName().equals(logicType)).findFirst().get();

            switch(trigLogicEnum){
                case OnLongerThanTime:
                     logic = new TimeAndInputsTriggerLogic.Builder(this.description, this.logicType)
                             .withDuration(this.duration.getMillis())
                             .withTrackedInput(this.trackedInput)
                             .withTrigger(trigger)
                             .build();
                    break;

                case OffLongerThanTime:
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
