package dycmaster.rysiek.triggers;


import dycmaster.rysiek.triggers.triggerParsers.TriggerParametersParser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;


public abstract class DefaultTrigger implements Trigger {

	Logger logger = Logger.getLogger(DefaultTrigger.class);
	Date _lastStateChangeTime = new Date();
	private Collection<TriggerListener> _triggerListeners =
			CollectionUtils.synchronizedCollection(new LinkedList<TriggerListener>());
	private volatile boolean _state = false;
	private String _name;
	private String _description;
	private TriggerParametersParser triggerParametersParser;
	private Collection<String> _possibleTriggerParameters;
	private boolean isEnabled;

	public DefaultTrigger() {
	}

	public DefaultTrigger(TriggerParametersParser triggerParametersParser) {
		this.triggerParametersParser = triggerParametersParser;
	}

	@Override
	public Collection<String> getPossibleTriggerParameters() {
		return _possibleTriggerParameters;
	}

	public void setPossibleTriggerParameters(Collection<String> possibleTriggerParameters) {
		_possibleTriggerParameters = possibleTriggerParameters;
	}

	public TriggerParametersParser getTriggerParametersParser() {
		return triggerParametersParser;
	}

	public void setTriggerParametersParser(TriggerParametersParser triggerParametersParser) {

		this.triggerParametersParser = triggerParametersParser;
		setPossibleTriggerParameters(triggerParametersParser.getPossibleParameters());
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}


	@Override
	public void start(){
		this.isEnabled = true;
	}

	@Override
	public void stop(){
		this.isEnabled = false;
	}


	protected void triggerInputChanged() {
		Map<String, String> triggerParameters = getTriggerInput();
		TriggersManager.getInstance().
				executeTriggerLogic(processSensorParametersToTriggerState(triggerParameters, getTriggerParametersParser()));
	}

	//implementation depends on a trigger
	protected abstract Map<String, String> getTriggerInput();

	protected Runnable processSensorParametersToTriggerState(final Map<String, String> sensorParameters,
	                                                         final TriggerParametersParser parser) {
		return new Runnable() {
			@Override
			public void run() {
				boolean state = parser.parseParametersToState(sensorParameters);
				setTriggerState(state);
			}
		};
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

	@Override
	public synchronized boolean getTriggerState() {
		return _state;
	}

	@Override
	public synchronized void setTriggerState(boolean state) {
		if (state != _state) {
			_state = state;
			_lastStateChangeTime = new Date();
			logger.info("trigger goes " + state);
			TriggerValue triggerValue = new TriggerValue(this, state);
			triggerStateChangedEvent(triggerValue);
		}
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

	/**
	 * Fire an event to notify subscribers
	 *
	 * @param triggerValue
	 */
	protected void triggerStateChangedEvent(final TriggerValue triggerValue) {
		synchronized (triggerValue) {
			for (TriggerListener triggerListener : _triggerListeners) {
				triggerListener.triggerValueChangedHandler(triggerValue);
			}
		}
	}

}
