package dycmaster.rysiek.sensors.sensorParsers;

import dycmaster.rysiek.sensors.SensorValue;
import sun.management.resources.agent_pt_BR;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by frs on 2/24/14.
 */
public class TempSensorParser extends  SensorParser {
	private  String[] availableParams = {"TEMP"};

	@Override
	public Map<String, String> parse(SensorValue sensorValue) {
		Map<String, String> res = new HashMap<>();
		res.put(availableParams[0], sensorValue.getSensorData().get(0));
		return  res;
	}

	@Override
	public Collection<String> getAvailableParameters() {
		return Arrays.asList(availableParams);
	}
}
