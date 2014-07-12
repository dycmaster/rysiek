package dycmaster.rysiek.sensors.sensorParsers;

import dycmaster.rysiek.sensors.SensorValue;
import dycmaster.rysiek.shared.Create;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by frs on 7/12/14.
 */
public class EchoSensorParser extends SensorParser {
    private  String[] availableParams = {"ECHO"};

    @Override
    public Map<String, String> parse(SensorValue sensorValue) {
        Map<String, String> res = Create.newMap();
        String fileContent =  sensorValue.getSensorData().stream().collect(Collectors.joining());
        res.put(availableParams[0], fileContent);
        return  res;
    }

    @Override
    public Collection<String> getAvailableParameters() {
        return Arrays.asList(availableParams);
    }
}
