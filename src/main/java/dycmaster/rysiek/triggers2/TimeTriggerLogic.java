package dycmaster.rysiek.triggers2;


import org.joda.time.DateTime;

public abstract class TimeTriggerLogic extends AbstractTriggerLogic {

    private final TimeOnlyTrigger trigger;
    private final DateTime dateTime;

    public TimeTriggerLogic(String description, TimeOnlyTrigger trigger, DateTime dateTime) {
        super(description);
        this.trigger = trigger;
        this.dateTime = dateTime;
    }

    protected  void updateTriggerState(boolean newState){
        trigger.updateTriggerStateBasedOnLogic(newState);
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    protected TimeOnlyTrigger getTrigger(){
        return  this.trigger;
    }

}
