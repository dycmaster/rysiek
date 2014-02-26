package dycmaster.rysiek.triggers;

import java.util.Map;

public class CompoundTrigger extends DefaultTrigger {

	//TODO add logic to combine many triggers

	public CompoundTrigger(String name) {
		setName(name);
	}

	@Override
	protected Map<String, String> getTriggerInput() {
		return null;
	}
}
