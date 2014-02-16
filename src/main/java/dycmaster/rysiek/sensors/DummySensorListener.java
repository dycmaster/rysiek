package dycmaster.rysiek.sensors;

import org.apache.log4j.Logger;

/**
 * Created by frs on 2/16/14.
 */
public final class DummySensorListener extends  AbstractSensorListener {

	Logger logger = Logger.getLogger(DummySensorListener.class);

	public DummySensorListener(Sensor sensor) {
		super(sensor);
	}

	public void sensorValueChangedHandler(SensorValue sensorValue) {
		logger.info("sensor value has changed! Value is: "+sensorValue.getSensorData().get(0));
	}
}
