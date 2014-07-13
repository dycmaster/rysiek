package dycmaster.rysiek.triggers2.logics.implementations;

import dycmaster.rysiek.triggers2.InputTriggerLogic;

import java.util.Map;


public class FlipFlopInputLogic extends InputTriggerLogic {

    private boolean currState;

    public FlipFlopInputLogic(String description) {
        super(description);
    }

    @Override
    public boolean processTriggerInputs(Map<String, Boolean> triggerInputStates, Map<Integer, String> triggerDeclaredInputs) {
        currState = !currState;
        return  currState;
    }


}
