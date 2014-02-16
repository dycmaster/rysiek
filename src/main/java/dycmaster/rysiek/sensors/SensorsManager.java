package dycmaster.rysiek.sensors;


import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.LinkedList;

public class SensorsManager {

	private static final Collection<Sensor> _availableSensors = CollectionUtils.synchronizedCollection(new LinkedList<>());

	public Collection<Sensor> getAvailableSensors() {
		return _availableSensors;
	}

	public void addAvailableSensor(Sensor sensor) {
		_availableSensors.add(sensor);
	}


	public void removeSensor(Sensor sensor) {
		sensor.stopObserving();
		sensor.removeSubscribers(sensor.getAllSubscribers());
		_availableSensors.remove(sensor);
	}

	public void removeSensors(Collection<Sensor> toRemove) {
		for(Sensor sensor: toRemove){
			removeSensor(sensor);
		}
	}


}


