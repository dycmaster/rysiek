package dycmaster.rysiek.triggers2;


import dycmaster.rysiek.shared.Create;
import javafx.util.Pair;

import java.util.List;
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


    public static class Builder{
        private final Map<Integer, String> declaredInputs;
        private final String name;
        private String logicType;
        private String logicDescription;
        private TruthTable truthTable;

        public Builder(Map<Integer, String> declaredInputs, String name){
            this.declaredInputs = declaredInputs;
            this.name = name;
        }

        public Builder withLogicType(String logicType){
            this.logicType = logicType;
            return  this;
        }

        public Builder withLogicDescription(String logicDescription){
            this.logicDescription = logicDescription;
            return  this;
        }

        public Builder withTruthTable(String[] header, List<Pair<Boolean[], Boolean>> rows){
            TruthTable currTable = new TruthTable(header);
            for (Pair<Boolean[], Boolean> row : rows) {
                currTable.addRow(row.getKey(), row.getValue());
            }
            this.truthTable = currTable;
            return  this;
        }

        public Builder withTruthTable(TruthTable truthTable){
            this.truthTable = truthTable;
            return  this;
        }

        public Builder withTruthTable(String singleLineTable, String[] inputNames){
            this.truthTable = new TruthTable(singleLineTable, inputNames);
            return  this;
        }

        public InputOnlyTrigger build(){
            InputOnlyTrigger trigger = new InputOnlyTrigger(declaredInputs, name);
            trigger.startTriggering();
            InputTriggerLogic logic;

            TriggerLogic logicTypeEnum = Create.newCollection(TriggerLogic.values()).stream()
                    .filter(x->x.getLogicStringName().equals(logicType)).findFirst().get();

            switch (logicTypeEnum){
                case TruthTableInputLogic:
                    logic = new TruthTableInputLogic(logicDescription, truthTable);
                    break;

                case FlipFlopInputLogic:
                    logic = new FlipFlopInputLogic(logicDescription);
                    break;

                default:
                    throw new RuntimeException("Unknown logic type: "+logicType);
            }

            trigger.setTriggerLogic(logic);
            return trigger;
        }

    }

}
