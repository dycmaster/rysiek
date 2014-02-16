package dycmaster.rysiek.actions;

import java.util.List;

/**
 * Created by frs on 2/16/14.
 */
public class SensorValue {

	public List<String> getSensorData() {
		return _sensorData;
	}

	private final List<String> _sensorData;


	public SensorValue(List<String> sensorData){
		this._sensorData = sensorData;
	}

}
