package dycmaster.rysiek.triggers2;


import dycmaster.rysiek.shared.Create;

import java.util.Map;

public class InputOnlyTrigger extends AbstractTrigger {
    private final Map<Integer,String> declaredInputs;   //number, name
    private Map<String, Integer> inputNumbers;  //name, number
    private Map<String, Boolean> inputStates;   //name, state
    private InputTriggerLogic inputOnlyTriggerLogic;

    public InputOnlyTrigger(Map<Integer, String> declaredInputs, String name) {
        super(name);
        this.declaredInputs = declaredInputs;
        initializeInputs(declaredInputs);
    }

    public void setTriggerLogic(InputTriggerLogic triggerLogic){
        super.setTriggerLogic(triggerLogic);
        this.inputOnlyTriggerLogic = triggerLogic;
    }

    private void initializeInputs(Map<Integer, String> inputsMap) {

        //reverse map
        this.inputNumbers = Create.newMap();
        for(Integer key: inputsMap.keySet()){
            inputNumbers.put(inputsMap.get(key), key);
        }

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
        if(isEnabled() && inputOnlyTriggerLogic!=null) {
            boolean newState = inputOnlyTriggerLogic.processTriggerInputs(inputStates, declaredInputs);
            updateTriggerStateBasedOnLogic(newState);
        }
    }

}
