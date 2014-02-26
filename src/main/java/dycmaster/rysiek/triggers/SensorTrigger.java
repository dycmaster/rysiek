package dycmaster.rysiek.triggers;


import dycmaster.rysiek.sensors.SensorInterpreter;
import dycmaster.rysiek.sensors.SensorListener;
import dycmaster.rysiek.triggers.triggerParsers.TriggerParametersParser;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/*
To use you have to:
1) override the logic for parsing sensor raw data to sensor parameters
2) provide the list of possible sensor parameters
3) deliver an object containing the parsing logic
 */
public class SensorTrigger extends DefaultTrigger implements SensorInterpreter {

	protected Logger logger;
	private SensorListener sensorListener;
	private volatile Map<String, String> _currTriggerParameters = new HashMap<>();


	public SensorTrigger() {
	}

	public SensorTrigger(SensorListener sensorListener, TriggerParametersParser triggerParametersParser) {
		setSensorListener(sensorListener);
		setTriggerParametersParser(triggerParametersParser);
		getSensorListener().addSubscriber(this);
	}

	@Override
	protected Map<String, String> getTriggerInput() {
		return _currTriggerParameters;
	}

	@Override
	public SensorListener getSensorListener() {
		return sensorListener;
	}

	@Override
	public void setSensorListener(SensorListener sensorListener) {
		this.sensorListener = sensorListener;
	}

	@Override
	public void sensorParametersChangedHandler(Map<String, String> parametersMap) {
		if (isEnabled()) {
			_currTriggerParameters = parametersMap;
			triggerInputChanged();
		}
	}


}
