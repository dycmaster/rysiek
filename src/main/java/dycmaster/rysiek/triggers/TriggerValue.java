package dycmaster.rysiek.triggers;

/**
 * Created by frs on 2/16/14.
 */
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
