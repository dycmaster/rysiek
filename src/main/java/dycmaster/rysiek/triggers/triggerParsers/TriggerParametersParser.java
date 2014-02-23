package dycmaster.rysiek.triggers.triggerParsers;

import java.util.Collection;
import java.util.Map;


public interface TriggerParametersParser {

	public boolean parseParametersToState
			(Map<String, String> parameters, Collection<String> availableParameters);
}
