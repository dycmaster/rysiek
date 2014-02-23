package dycmaster.rysiek.triggers;


import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;



public  class DefaultTrigger implements Trigger {

	Logger logger = Logger.getLogger(DefaultTrigger.class);
	Date _lastStateChangeTime = new Date();
	private Collection<TriggerListener> _triggerListeners =
			CollectionUtils.synchronizedCollection(new LinkedList<TriggerListener>());
	private volatile boolean _state;
	private String _name;
	private String _description;


	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

	@Override
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	private boolean isEnabled;



	@Override
	public void subscribeToTrigger(TriggerListener listener) {
		_triggerListeners.add(listener);
	}

	@Override
	public void removeSubscriber(TriggerListener listener) {
		_triggerListeners.remove(listener);
	}

	@Override
	public Collection<TriggerListener> getAllSubscribers() {
		return _triggerListeners;
	}

	@Override
	public void removeSubscribers(Collection<TriggerListener> toRemove) {
		_triggerListeners.removeAll(toRemove);
	}

	@Override
	public boolean getTriggerState() {
		return _state;
	}

	@Override
	public void setTriggerState(boolean state) {
		if (state != _state) {
			_lastStateChangeTime = new Date();
			logger.info("trigger goes " + state);
		}
		_state = state;
	}

	@Override
	public Date getLastStateChangeTime() {
		return _lastStateChangeTime;
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public void setName(String name) {
		_name = name;
	}

	@Override
	public String getDescription() {
		return _description;
	}

	@Override
	public void setDescription(String description) {
		_description = description;
	}

}
