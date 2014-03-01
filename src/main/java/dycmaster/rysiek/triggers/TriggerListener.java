package dycmaster.rysiek.triggers;

import java.util.Collection;


public interface TriggerListener {

	public void triggerValueChangedHandler(TriggerValue triggerValue);
	public void start();
	public void stop();
	public boolean isEnabled();

}
