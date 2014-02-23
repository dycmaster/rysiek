package dycmaster.rysiek.sensors.sensorParsers;

import dycmaster.rysiek.sensors.SensorValue;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;



public class FlipFlopSensorParser extends SensorParser{
	private boolean state = false;
	private String [] availableParameters = {"state"};
	Logger logger = Logger.getLogger("flipFlop Sensor Parser: ");


	public Map<String, String> parse(SensorValue sensorValue) {
		Map<String, String> res = new HashMap<>();
		state = !state;
		res.put(availableParameters[0], Boolean.toString(state));
		printParameterValues(res, logger);
		return res;
	}


	@Override
	public Collection<String> getAvailableParameters() {
		return Arrays.asList(availableParameters);
	}
}
