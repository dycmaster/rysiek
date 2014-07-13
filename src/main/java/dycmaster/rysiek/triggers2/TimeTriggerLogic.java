package dycmaster.rysiek.triggers2;


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

}
