package dycmaster.rysiek.actions;

/**
 * Created by frs on 2/16/14.
 */
public interface SensorListener {

    public void sensorValueChangedHandler(SensorValue sensorValue);
	public void startListening();
	public void stopListening();

}
