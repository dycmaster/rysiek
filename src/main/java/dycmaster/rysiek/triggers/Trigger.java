package dycmaster.rysiek.triggers;


import java.util.Collection;
import java.util.Date;


public interface Trigger {

	public void subscribeToTrigger(TriggerListener listener);
	public void removeSubscriber(TriggerListener listener);
	public Collection<TriggerListener> getAllSubscribers();
	public void removeSubscribers(Collection<TriggerListener> toRemove);
	public boolean getTriggerState();
	public void setTriggerState(boolean state);
	public Date getLastStateChangeTime();
	public String getName();
	public void  setName(String name);
	public String getDescription();
	public void setDescription(String description);
	public void start();
	public void stop();
	public boolean isEnabled();


	public Collection<String> getPossibleTriggerParameters();




}
