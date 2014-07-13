package dycmaster.rysiek.triggers;


import java.util.Collection;
import java.util.Date;


public interface Trigger {

    //subscribers
	public void subscribeToTrigger(TriggerListener listener);
	public void removeSubscriber(TriggerListener listener);
	public Collection<TriggerListener> getAllSubscribers();
	public void removeSubscribers(Collection<TriggerListener> toRemove);

    //descriptive
    public String getName();
    public void  setName(String name);
    public String getDescription();
    public void setDescription(String description);

    //essence
    public boolean getTriggerState();
	public void setTriggerState(boolean state);
	public Date getLastStateChangeTime();
    public Collection<String> getPossibleTriggerParameters();

    //bla bla..
	public void start();
	public void stop();
	public boolean isEnabled();




}
