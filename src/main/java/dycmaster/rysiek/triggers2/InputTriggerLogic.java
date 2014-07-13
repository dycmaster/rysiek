package dycmaster.rysiek.triggers2;

import java.util.Map;

public abstract class InputTriggerLogic extends AbstractTriggerLogic {


    public InputTriggerLogic(String description) {
        super(description);
    }

    public abstract boolean processTriggerInputs(Map<String, Boolean> triggerInputStates, Map<Integer, String> triggerDeclaredInputs);
}
