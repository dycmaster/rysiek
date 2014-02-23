package dycmaster.rysiek.triggers;


import dycmaster.rysiek.sensors.SensorInterpreter;
import dycmaster.rysiek.sensors.SensorListener;
import dycmaster.rysiek.triggers.triggerParsers.TriggerParametersParser;
import org.apache.log4j.Logger;

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
	private TriggerParametersParser triggerParametersParser;



	public SensorTrigger(SensorListener sensorListener, TriggerParametersParser triggerParametersParser) {
		setSensorListener(sensorListener);
		setTriggerParametersParser(triggerParametersParser);
		getSensorListener().addSubscriber(this);
	}

	@Override
	public void sensorParametersChangedHandler(Map<String, String> parametersMap) {
		if ( isEnabled() ) {
			TriggersManager.getInstance().
					executeTriggerLogic(processSensorParametersToTriggerState(parametersMap, getTriggerParametersParser()));
		}
	}


	protected Runnable processSensorParametersToTriggerState(final Map<String, String> sensorParameters,
	                                                         final TriggerParametersParser parser) {
		return new Runnable() {
			@Override
			public void run() {
				boolean state = parser.parseParametersToState(sensorParameters, getSensorListener().getAvailableParameters());
				setTriggerState(state);
			}
		};
	}


	public SensorListener getSensorListener() {
		return sensorListener;
	}

	public void setSensorListener(SensorListener sensorListener) {
		this.sensorListener = sensorListener;
	}

	public TriggerParametersParser getTriggerParametersParser() {
		return triggerParametersParser;
	}

	public void setTriggerParametersParser(TriggerParametersParser triggerParametersParser) {
		this.triggerParametersParser = triggerParametersParser;
	}

}
