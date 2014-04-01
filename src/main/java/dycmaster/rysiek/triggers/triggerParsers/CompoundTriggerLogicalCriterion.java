package dycmaster.rysiek.triggers.triggerParsers;


import dycmaster.rysiek.shared.Create;
import dycmaster.rysiek.triggers.Trigger;
import org.apache.commons.collections.CollectionUtils;

import java.util.Date;
import java.util.Set;

public class CompoundTriggerLogicalCriterion {

	public enum Kind{
		IS_ON,
		IS_OFF,
		GOES_ON,
		GOES_OFF,

		BEFORE_TIME,
		AFTER_TIME,
		AT_TIME,

		ON_DAY,
		ON_DAY_OF_MONTH,
		ON_DAY_OF_WEEK,

		IS_ON_LONGER_THAN,
		IS_ON_SHORTER_THAN,
		IS_ON_BEFORE_TIME,
		IS_ON_AFTER_TIME,
		IS_ON_AT_TIME,

		IS_OFF_LONGER_THAN,
		IS_OFF_SHORTER_THAN,
		IS_OFF_BEFORE_TIME,
		IS_OFF_AFTER_TIME,
		IS_OFF_AT_TIME,

		GOES_ON_BEFORE_TIME,
		GOES_ON_AFTER_TIME,
		GOES_ON_AT_TIME,

		GOES_OFF_BEFORE_TIME,
		GOES_OFF_AFTER_TIME,
		GOES_OFF_AT_TIME
	}

	public static final Set<Kind> TIME_ABSOLUTE_CRITERIA = Create.boxIntoSet(Kind.BEFORE_TIME, Kind.AFTER_TIME, Kind.AT_TIME);

	public CompoundTriggerLogicalCriterion(String trigger, Kind kind, int valueMs, Date time){
		_triggerName = trigger;
		_kind = kind;
		this.valueMs = valueMs;
		this.time = time;
	}

	public int getValueMs() {
		return valueMs;
	}

	public void setValueMs(int valueMs) {
		this.valueMs = valueMs;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	private int valueMs;
	private Date time;

	public String getTriggerName() {
		return _triggerName;
	}

	public void setTriggerName(String triggerName) {
		this._triggerName = triggerName;
	}

	public Kind getKind() {
		return _kind;
	}

	@Override
	public String toString(){
		return getKind().toString();
	}

	public void setKind(Kind _kind) {
		this._kind = _kind;
	}

	private String _triggerName;
	private Kind _kind;



}
