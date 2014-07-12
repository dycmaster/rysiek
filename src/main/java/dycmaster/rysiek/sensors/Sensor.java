package dycmaster.rysiek.sensors;

import dycmaster.rysiek.shared.Create;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.Date;

/**
 * Sensor observes something and can issue a signal when the underlying
 * observed changes it's state. Sensor copies the current output of the observed entity
 * and provides it to be processed by a trigger using some certain logic.
 *
 * 1. Detect the change
 * 2. Get new sensor value
 * 3. Inform subscribers by calling observedEntityChangedEvent
 */
public abstract class Sensor {

	private Collection<SensorListener> sensorListeners =
			CollectionUtils.<SensorListener>synchronizedCollection(Create.<SensorListener>newCollection());
	private String _name;
	private Date lastChangeTime;

	public abstract void startObserving();

	public abstract void stopObserving();

	public abstract  boolean isEnabled();


	/*
	 * You have to call this one when change is detected.
	 * Inside getChangedEventArgs has to be the sensor logic
	 */
	protected void onChangeDetected() {
		SensorValue sensorValue = getChangedEventArgs();
		observedEntityChangedEvent(sensorValue);
	}

	/*
	 * It has to be called after new sensor value has been detected.
	 * You have to overload this method to make it able to get the
	 * event args from a particular observed entity.
	 */
	protected abstract SensorValue getChangedEventArgs();


	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public void addSubscriber(SensorListener listener) {
		sensorListeners.add(listener);
	}

	public void removeSubscriber(SensorListener listener) {
		sensorListeners.remove(listener);
	}

	public void removeSubscribers(Collection<SensorListener> toRemove) {
		sensorListeners.removeAll(toRemove);
	}

	public Collection<SensorListener> getAllSubscribers() {
		return sensorListeners;
	}

	public Date getLastChangeTime() {
		return lastChangeTime;
	}

	/*
	 * Fire an event
	 */
	protected void observedEntityChangedEvent(final SensorValue sensorValue) {
		lastChangeTime = new Date();

		for (final SensorListener listener : sensorListeners) {
			listener.sensorValueChangedHandler(sensorValue);
		}

	}



}
