package dycmaster.rysiek.triggers;

import dycmaster.rysiek.sensors.SensorValue;
import dycmaster.rysiek.sensors.Sensor;

import java.util.Date;

/**
 * Created by frs on 2/16/14.
 */
public class MusicIsOnTrigger extends SensorTrigger {

	public MusicIsOnTrigger(Sensor sensor) {
		super(sensor);
	}

	private Date lastUsage = new Date();
	private int offThreshold = 3;

	@Override
	protected Runnable processSensorEvent(final SensorValue sensorValue) {
		return new  Runnable(){
			@Override
			public void run() {
				if(!sensorValue.getSensorData().get(0).equalsIgnoreCase("idle")){
					lastUsage = new Date();
					setTriggerState(true);
				}else{
					if(((new Date().getTime() - lastUsage.getTime())/(long)1000) > offThreshold ){
						setTriggerState(false);
					}
				}
			}
		};
	}

}
