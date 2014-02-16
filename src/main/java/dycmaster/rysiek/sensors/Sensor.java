package dycmaster.rysiek.sensors;

import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Sensor observes something and can issue a signal when the underlying
 * observed changes it's state. Sensor copies the current output of the observed entity
 * and provides it to be processed by a trigger using some certain logic.
 */
public abstract class Sensor {

	private Collection<SensorListener> sensorListeners = CollectionUtils.synchronizedCollection(new LinkedList<>());
	private String _name;

	public abstract void startObserving();
	public abstract void stopObserving();

	public  String getName(){
		return  _name;
	}

	public  void setName(String name){
		_name = name;
	}

	public void subscribeToSensor(SensorListener listener){
		sensorListeners.add(listener);
	}

	public void removeSubscriber(SensorListener listener){
		sensorListeners.remove(listener);
	}

	public void removeSubscribers(Collection<SensorListener> toRemove){
		sensorListeners.removeAll(toRemove);
	}

	public Collection<SensorListener> getAllSubscribers(){
		return sensorListeners;
	}

	/*
	 * Internal action when a change has been detected.
	 */
	protected  void observedEntityChanged(final SensorValue sensorValue){
		for(final SensorListener listener: sensorListeners){
			listener.sensorValueChangedHandler(sensorValue);
		}

		//running in the same thread for performance reasons..
//		for(final SensorListener listener: sensorListeners){
//			Runnable notifyListener = new Runnable() {
//				@Override
//				public void run() {
//					listener.sensorValueChangedHandler(sensorValue);
//				}
//			};
//			new Thread(notifyListener).start();
//		}
	}
}
