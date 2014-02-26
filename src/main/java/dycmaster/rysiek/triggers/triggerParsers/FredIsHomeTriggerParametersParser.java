package dycmaster.rysiek.triggers.triggerParsers;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;


public class FredIsHomeTriggerParametersParser implements TriggerParametersParser {

	String[] possibleParameters = {"userHome"};

	@Override
	public boolean parseParametersToState(Map<String, String> parameters) {
		return false;
	}

	@Override
	public Collection<String> getPossibleParameters() {
		return Arrays.asList(possibleParameters);
	}
}
