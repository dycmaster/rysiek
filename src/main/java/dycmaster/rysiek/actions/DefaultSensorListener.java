package dycmaster.rysiek.actions;


import org.apache.log4j.Logger;

public class DefaultSensorListener implements  SensorListener {

	Logger logger = Logger.getLogger(DefaultSensorListener.class);
	private Sensor mySensor;

	public void sensorValueChangedHandler(SensorValue sensorValue) {
		logger.info("Sensor value changed! New value: "+sensorValue.getSensorData().get(0));
	}

	@Override
	public void startListening() {
		mySensor.subscribeToSensor(this);
	}

	@Override
	public void stopListening() {
		mySensor.removeSubscriber(this);
	}

	public DefaultSensorListener(){

	}

	public DefaultSensorListener(Sensor sensor){
		mySensor  = sensor;
	}



}
