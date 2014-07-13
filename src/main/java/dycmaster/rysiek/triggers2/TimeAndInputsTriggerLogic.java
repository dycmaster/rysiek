package dycmaster.rysiek.triggers2;



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
}
