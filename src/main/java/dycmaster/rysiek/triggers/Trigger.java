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
	public void setEnabled(boolean isEnabled);
	public boolean isEnabled();

	/**
	 * Called internally when trigger's data source has changed
	 */
	public void triggerInputChanged();
	public Collection<String> getPossibleTriggerParameters();


}
