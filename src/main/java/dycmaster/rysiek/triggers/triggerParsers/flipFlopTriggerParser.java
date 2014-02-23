package dycmaster.rysiek.triggers.triggerParsers;


import java.util.Collection;
import java.util.Map;


public  class flipFlopTriggerParser implements TriggerParametersParser {

	private  boolean currState = false;

	@Override
	public boolean parseParametersToState
			(Map<String, String> parameters, Collection<String> availableParameters) {
		currState = !currState;
		return currState;
	}
}
