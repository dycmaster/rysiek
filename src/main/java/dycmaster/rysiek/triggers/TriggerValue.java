package dycmaster.rysiek.triggers;


public class TriggerValue {
	private  boolean _triggerStatus = false;
	private Trigger _sender;

	public TriggerValue(Trigger sender, boolean triggerStatus){
		_sender = sender;
		_triggerStatus  = triggerStatus;
	}

	public boolean getTriggerStatus(){
		return  _triggerStatus;
	}

	public Trigger getSender(){return  _sender; }

}
