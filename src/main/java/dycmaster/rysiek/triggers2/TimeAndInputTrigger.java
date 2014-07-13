package dycmaster.rysiek.triggers2;

import dycmaster.rysiek.shared.Create;

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

}
