package dycmaster.rysiek.triggers;

import dycmaster.rysiek.sensors.SensorValue;
import dycmaster.rysiek.sensors.Sensor;

/**
 * Created by frs on 2/16/14.
 */
public final class DummySensorTrigger extends  SensorTrigger {


	public DummySensorTrigger(Sensor sensor) {
		super(sensor);
	}


	@Override
	protected Runnable processSensorEvent(final SensorValue sensorValue) {
		return new  Runnable(){
			@Override
			public void run() {
				logger.info("PROCESSING SENSOR EVENT!!! (multithreaded, in Executor) value="+sensorValue.getSensorData().get(0));
			}
		};
	}

}
