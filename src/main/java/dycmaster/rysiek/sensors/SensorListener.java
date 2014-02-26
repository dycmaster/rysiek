package dycmaster.rysiek.sensors;

import java.util.Collection;
import java.util.Date;
import java.util.Map;


public interface SensorListener {

	public Collection<String> getAvailableParameters();
	public void setSensor(Sensor sensor);
	public Sensor getSensor();
    public void sensorValueChangedHandler(SensorValue sensorValue);
	public void startListening();
	public void stopListening();
	public boolean isListening();
	public Date getLastChangeTime();
	public Map<String,String> getParsedParameters();
	public void removeSubscriber(SensorInterpreter interpreter);
	public void addSubscriber(SensorInterpreter interpreter);

}
