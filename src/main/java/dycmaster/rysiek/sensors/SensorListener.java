package dycmaster.rysiek.sensors;

import java.util.Collection;
import java.util.Date;
import java.util.Map;


public interface SensorListener {

	/**
	 * Parameters which a given sensor is able to generate in general.
	 * @return
	 */
	public Collection<String> getAvailableParameters();
	public void setSensor(Sensor sensor);
	public Sensor getSensor();
    public void sensorValueChangedHandler(SensorValue sensorValue);
	public void start();
	public void stop();
	public boolean isEnabled();
	public Date getLastChangeTime();
	public Map<String,String> getParsedParameters();
	public void removeSubscriber(SensorInterpreter interpreter);
	public void addSubscriber(SensorInterpreter interpreter);

}
