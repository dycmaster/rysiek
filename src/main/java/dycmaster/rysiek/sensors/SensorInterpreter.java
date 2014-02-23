package dycmaster.rysiek.sensors;

import java.util.Map;


public interface SensorInterpreter {
	public void sensorParametersChangedHandler(Map<String,String> parametersMap);

}
