package dycmaster.rysiek.sensors.sensorParsers;

import dycmaster.rysiek.sensors.SensorValue;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class MusicOnSensorParser extends   SensorParser {
	private String [] availableParameters = {"isOn"};
	Logger  logger = Logger.getLogger(MusicOnSensorParser.class);

	public Map<String, String> parse(SensorValue sensorValue) {
		Map<String, String> res = new HashMap<>();
		String msg = sensorValue.getSensorData().get(0);
		if(msg.equalsIgnoreCase("busy")){
			res.put(availableParameters[0], "true");
		}else{
			res.put(availableParameters[0], "false");
		}
		SensorParser.printParameterValues(res, logger);
		return  res;
	}

	@Override
	public Collection<String> getAvailableParameters() {
		return Arrays.asList(availableParameters);
	}
}
