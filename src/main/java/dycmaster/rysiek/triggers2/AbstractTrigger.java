package dycmaster.rysiek.triggers2;



import com.google.common.collect.Iterables;
import dycmaster.rysiek.shared.Create;
import javafx.util.Pair;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.List;

abstract class AbstractTrigger implements  ITrigger {


    private ITriggerLogic triggerLogic;
    private final String name;
    private String description;
    private boolean enabled;
    private boolean triggerState;
    private List<Pair<DateTime, Boolean>> triggerHistory = Create.newList();
    Logger log = Logger.getLogger(AbstractTrigger.class);

    private Collection<ITriggerListener> subscribers =
            CollectionUtils.<ITriggerListener>synchronizedCollection(Create.<ITriggerListener>newCollection());


    public AbstractTrigger(String name) {
        this.name = name;
        triggerHistory.add(new Pair<>(new DateTime(), triggerState));
    }


    public void setTriggerLogic(ITriggerLogic triggerLogic) {
        this.triggerLogic = triggerLogic;
    }


    @Override
    public ITriggerLogic getTriggerLogic() {
        return triggerLogic;
    }

    private void setTriggerState(boolean newState){
        if (newState != triggerState) {
            triggerState = newState;
            triggerHistory.add(new Pair<>(new DateTime(), triggerState));
            triggerOutputChangedEvent();
            log.debug(String.format("Trigger '%s' went '%s'", getName(), newState));
        }
    }

    //this is called from triggerLogic
    protected void updateTriggerStateBasedOnLogic(boolean newOutput){
        if(isEnabled()){
            setTriggerState(newOutput);
        }
    }

    @Override
    public boolean getOutputState() {
        return triggerState;
    }

    @Override
    public DateTime getLastChangeTime() {
        return Iterables.getLast(triggerHistory, null).getKey();
    }

    @Override
    public List<Pair<DateTime, Boolean>> getTriggerHistory() {
        return triggerHistory;
    }

    @Override
    public void startTriggering() {
        enabled = true;
    }

    @Override
    public void stopTriggering() {
        enabled = false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void addListener(ITriggerListener subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void removeListener(ITriggerListener subscriber) {
        subscribers.remove(subscriber);
    }

    protected void triggerOutputChangedEvent(){
        TriggerOutputChangedEventArgs args = new TriggerOutputChangedEventArgs(this, triggerState);
        subscribers.forEach( (subscriber) -> subscriber.triggerOutputChangedEventHandler(args) );
    }

}
