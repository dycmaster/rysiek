package dycmaster.rysiek.sensors;

import dycmaster.rysiek.sensors.sensorParsers.SensorParser;

import java.util.Collection;
import java.util.Date;
import java.util.Map;


public interface SensorListener {

	/**
	 * Parameters which a given sensor is able to generate in general.
	 * @return
	 */
	public Collection<String> getAvailableParameters();
    public void sensorValueChangedHandler(SensorValue sensorValue);
	public void startListening();
	public void stopListening();
	public boolean isEnabled();
	public Date getLastChangeTime();

    void setSensorParser(SensorParser sensorParser);
    public Map<String,String> getParsedParameters();
	public void removeSubscriber(SensorInterpreter interpreter);
	public void addSubscriber(SensorInterpreter interpreter);

}
