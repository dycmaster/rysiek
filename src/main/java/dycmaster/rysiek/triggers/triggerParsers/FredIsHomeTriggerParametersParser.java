package dycmaster.rysiek.triggers.triggerParsers;

import java.util.Collection;
import java.util.Map;


public class FredIsHomeTriggerParametersParser implements TriggerParametersParser {

	@Override
	public boolean parseParametersToState
			(Map<String, String> parameters, Collection<String> availableParameters) {
		return false;
	}
}
