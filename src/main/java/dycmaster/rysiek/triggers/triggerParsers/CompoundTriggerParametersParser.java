package dycmaster.rysiek.triggers.triggerParsers;


import java.util.Map;

public interface CompoundTriggerParametersParser {
	public  boolean parseParametersToState(Map<String, Boolean> parameters, CompoundTriggerLogicalCriteria logicalCriteria);
}
