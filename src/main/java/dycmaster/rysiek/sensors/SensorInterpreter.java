package dycmaster.rysiek.sensors;

import java.util.Map;


public interface SensorInterpreter {
	public void sensorParametersChangedHandler(Map<String,String> parametersMap);
	public SensorListener getSensorListener();
	public void setSensorListener(SensorListener sensorListener);

}
