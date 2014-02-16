package dycmaster.rysiek.triggers;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by frs on 2/16/14.
 */
public class BasicTrigger implements Trigger {

	private Collection<TriggerListener> _triggerListeners = CollectionUtils.synchronizedCollection(new LinkedList<>());
	protected Logger logger;

	public BasicTrigger(String name) {
		setName(name);
		logger = Logger.getLogger(getName());
	}

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


	private boolean _state;
	@Override
	public boolean getTriggerState() {
		return _state;
	}

	@Override
	public  void setTriggerState(boolean state){
		if(state!=_state){
			_lastStateChangeTime = new Date();
			logger.info("trigger goes "+state);
		}
		_state = state;
	}

	Date _lastStateChangeTime = new Date();
	@Override
	public Date getLastStateChangeTime() {
		return _lastStateChangeTime;
	}



	private String _name;
	@Override
	public String getName() {
		return _name;
	}

	@Override
	public void setName(String name) {
		_name = name;
	}

	private String _description;
	@Override
	public String getDescription() {
		return _description;
	}

	@Override
	public void setDescription(String description) {
		_description = description;
	}
}
