package dycmaster.rysiek.triggers;


public abstract class AbstractTriggerListener implements  TriggerListener {

	private boolean _isEnabled = false;

	protected abstract  void  triggerValueChangedAction(TriggerValue triggerValue);

	@Override
	public void triggerValueChangedHandler(TriggerValue triggerValue) {
		if(_isEnabled){
			triggerValueChangedAction(triggerValue);
		}
	}

	@Override
	public void start() {
		_isEnabled = true;
	}

	@Override
	public void stop() {
		_isEnabled = false;
	}

	@Override
	public boolean isEnabled() {
		return _isEnabled;
	}
}
