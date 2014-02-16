package dycmaster.rysiek.sensors;

/**
 * Created by frs on 2/16/14.
 */
public interface SensorListener {

	public void setSensor(Sensor sensor);
	public Sensor getSensor();
    public void sensorValueChangedHandler(SensorValue sensorValue);
	public void startListening();
	public void stopListening();
	public boolean isListening();

}
