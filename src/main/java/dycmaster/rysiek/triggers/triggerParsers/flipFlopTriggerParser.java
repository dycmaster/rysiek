package dycmaster.rysiek.triggers.triggerParsers;


import java.util.Arrays;
import java.util.Collection;
import java.util.Map;


public  class flipFlopTriggerParser implements TriggerParametersParser {

	String[] possibleParameters = {"flipFlop"};
	private  boolean currState = false;

	@Override
	public boolean parseParametersToState(Map<String, String> parameters) {
		currState = !currState;
		return currState;
	}

	@Override
	public Collection<String> getPossibleParameters() {
		return Arrays.asList(possibleParameters);
	}
}
