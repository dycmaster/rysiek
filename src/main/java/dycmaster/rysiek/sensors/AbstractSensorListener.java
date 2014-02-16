package dycmaster.rysiek.sensors;


public abstract class AbstractSensorListener implements  SensorListener {

	private Sensor mySensor;
	private  boolean isListening;

	@Override
	public void setSensor(Sensor sensor) {
		mySensor = sensor;
	}

	@Override
	public Sensor getSensor() {
		return mySensor;
	}

	@Override
	public abstract void sensorValueChangedHandler(SensorValue sensorValue);



	@Override
	public void startListening() {
		mySensor.subscribeToSensor(this);
		isListening = true;
	}

	@Override
	public void stopListening() {
		mySensor.removeSubscriber(this);
		isListening = false;
	}

	@Override
	public boolean isListening() {
		return isListening;
	}

	public AbstractSensorListener(Sensor sensor){
		setSensor(sensor);
	}
}
