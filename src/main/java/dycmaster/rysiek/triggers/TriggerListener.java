package dycmaster.rysiek.triggers;

import java.util.Collection;

/**
 * Created by frs on 2/16/14.
 */
public interface TriggerListener {

	public void addTriggerToListen(Trigger trigger);
	public Collection<Trigger> getTriggers();
	public void triggerValueChangedHandler(TriggerValue triggerValue);
	public void startListening();
	public void stopListening();
	public boolean isListening();

}
