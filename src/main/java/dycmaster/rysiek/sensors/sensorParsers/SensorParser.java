package dycmaster.rysiek.sensors.sensorParsers;

import dycmaster.rysiek.sensors.SensorValue;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Map;



public abstract class SensorParser {
	public abstract Map<String, String> parse(SensorValue sensorValue);
	public abstract Collection<String> getAvailableParameters();

	public static void printParameterValues(Map<String, String> paramValueMap, Logger logger) {
		for (String key : paramValueMap.keySet()) {
			String val = paramValueMap.get(key);
			logger.debug(key + "=" + val);
		}
	}
}
