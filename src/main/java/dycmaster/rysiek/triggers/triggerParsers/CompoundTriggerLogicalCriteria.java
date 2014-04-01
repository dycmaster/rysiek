package dycmaster.rysiek.triggers.triggerParsers;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class CompoundTriggerLogicalCriteria {
	private List<CompoundTriggerLogicalCriterion> _criteria = new LinkedList<>();

	public Collection<CompoundTriggerLogicalCriterion> getAllCriteria(){
		return  _criteria;
	}

	public CompoundTriggerLogicalCriteria and(CompoundTriggerLogicalCriteria criterion){
		Collection<CompoundTriggerLogicalCriterion> toAdd =  CollectionUtils.subtract(criterion.getAllCriteria(), this._criteria);
		_criteria.addAll(toAdd);
		return  this;
	}

	public CompoundTriggerLogicalCriteria isOn(String trigger){
		_criteria.add(new CompoundTriggerLogicalCriterion(trigger, CompoundTriggerLogicalCriterion.Kind.IS_ON, 0, null));
		return  this;
	}

	public CompoundTriggerLogicalCriteria isOff(String trigger){
		_criteria.add(new CompoundTriggerLogicalCriterion(trigger, CompoundTriggerLogicalCriterion.Kind.IS_OFF, 0, null));
		return  this;
	}

	public CompoundTriggerLogicalCriteria goesOff(String trigger){
		_criteria.add(new CompoundTriggerLogicalCriterion(trigger, CompoundTriggerLogicalCriterion.Kind.GOES_OFF, 0, null));
		return this;
	}

	public CompoundTriggerLogicalCriteria goesOn(String trigger){
		_criteria.add(new CompoundTriggerLogicalCriterion(trigger, CompoundTriggerLogicalCriterion.Kind.GOES_ON, 0, null));
		return this;
	}

	public CompoundTriggerLogicalCriteria beforeTime(Date time){
		_criteria.add(new CompoundTriggerLogicalCriterion("", CompoundTriggerLogicalCriterion.Kind.BEFORE_TIME, 0, time));
		return this;
	}

	public CompoundTriggerLogicalCriteria afterTime(Date time){
		_criteria.add(new CompoundTriggerLogicalCriterion("", CompoundTriggerLogicalCriterion.Kind.AFTER_TIME, 0, time));
		return this;
	}

	//wink ;) e.g. 5s on
	public CompoundTriggerLogicalCriteria atTime(Date time){
		_criteria.add(new CompoundTriggerLogicalCriterion("", CompoundTriggerLogicalCriterion.Kind.AT_TIME, 0, time));
		return this;
	}

	//takes into account exact date, not only day number
	public CompoundTriggerLogicalCriteria onDay(Date day){
		_criteria.add(new CompoundTriggerLogicalCriterion("", CompoundTriggerLogicalCriterion.Kind.ON_DAY, 0, day));
		return  this;
	}

	public CompoundTriggerLogicalCriteria onDayOfWeek(Date day){
		_criteria.add(new CompoundTriggerLogicalCriterion("", CompoundTriggerLogicalCriterion.Kind.ON_DAY_OF_WEEK, 0, day));
		return  this;
	}

	public CompoundTriggerLogicalCriteria onDayOfMonth(Date day){
		_criteria.add(new CompoundTriggerLogicalCriterion("", CompoundTriggerLogicalCriterion.Kind.ON_DAY_OF_MONTH, 0, day));
		return  this;
	}



	///////////////////////
	// IS ON TRIGGERS
	public CompoundTriggerLogicalCriteria isOnLongerThan(String trigger, int ms){
		_criteria.add(new CompoundTriggerLogicalCriterion(trigger, CompoundTriggerLogicalCriterion.Kind.IS_ON_LONGER_THAN , ms, null));
		return this;
	}

	public CompoundTriggerLogicalCriteria isOnShorterThan(String trigger, int ms){
		_criteria.add(new CompoundTriggerLogicalCriterion(trigger, CompoundTriggerLogicalCriterion.Kind.IS_ON_SHORTER_THAN , ms, null));
		return this;
	}

	public CompoundTriggerLogicalCriteria isOnBeforeTime(String trigger, Date time){
		_criteria.add(new CompoundTriggerLogicalCriterion(trigger, CompoundTriggerLogicalCriterion.Kind.IS_ON_BEFORE_TIME , 0, time));
		return this;
	}

	public CompoundTriggerLogicalCriteria isOnAfterTime(String trigger, Date time){
		_criteria.add(new CompoundTriggerLogicalCriterion(trigger, CompoundTriggerLogicalCriterion.Kind.IS_ON_AFTER_TIME , 0, time));
		return this;
	}

	public CompoundTriggerLogicalCriteria isOnAtTime(String trigger, Date time){
		_criteria.add(new CompoundTriggerLogicalCriterion(trigger, CompoundTriggerLogicalCriterion.Kind.IS_ON_AT_TIME , 0, time));
		return this;
	}

	//////////////
	//IS OFF TRIGGERS
	public CompoundTriggerLogicalCriteria isOffLongerThan(String trigger, int ms){
		_criteria.add(new CompoundTriggerLogicalCriterion(trigger, CompoundTriggerLogicalCriterion.Kind.IS_OFF_LONGER_THAN , ms, null));
		return this;
	}

	public CompoundTriggerLogicalCriteria isOffShorterThan(String trigger, int ms){
		_criteria.add(new CompoundTriggerLogicalCriterion(trigger, CompoundTriggerLogicalCriterion.Kind.IS_OFF_SHORTER_THAN , ms, null));
		return this;
	}

	public CompoundTriggerLogicalCriteria isOffBeforeTime(String trigger, Date time){
		_criteria.add(new CompoundTriggerLogicalCriterion(trigger, CompoundTriggerLogicalCriterion.Kind.IS_OFF_BEFORE_TIME , 0, time));
		return this;
	}

	public CompoundTriggerLogicalCriteria isOffAfterTime(String trigger, Date time){
		_criteria.add(new CompoundTriggerLogicalCriterion(trigger, CompoundTriggerLogicalCriterion.Kind.IS_OFF_AFTER_TIME , 0, time));
		return this;
	}

	public CompoundTriggerLogicalCriteria isOffAtTime(String trigger, Date time){
		_criteria.add(new CompoundTriggerLogicalCriterion(trigger, CompoundTriggerLogicalCriterion.Kind.IS_OFF_AT_TIME , 0, time));
		return this;
	}

	/////////////////////////
	// GOES ON TRIGGERS
	//when the trigger goes on e.g. after 9pm
	public CompoundTriggerLogicalCriteria goesOnBeforeTime(String trigger, Date time){
		_criteria.add(new CompoundTriggerLogicalCriterion(trigger, CompoundTriggerLogicalCriterion.Kind.GOES_ON_BEFORE_TIME, 0, time));
		return  this;
	}

	public CompoundTriggerLogicalCriteria goesOnAfterTime(String trigger, Date time){
		_criteria.add(new CompoundTriggerLogicalCriterion(trigger, CompoundTriggerLogicalCriterion.Kind.GOES_ON_AFTER_TIME, 0, time));
		return  this;
	}

	public CompoundTriggerLogicalCriteria goesOnAtTime(String trigger, Date time){
		_criteria.add(new CompoundTriggerLogicalCriterion(trigger, CompoundTriggerLogicalCriterion.Kind.GOES_ON_AT_TIME, 0, time));
		return  this;
	}


	/////////////////////////
	// GOES OFF TRIGGERS
	public CompoundTriggerLogicalCriteria goesOffBeforeTime(String trigger, Date time){
		_criteria.add(new CompoundTriggerLogicalCriterion(trigger, CompoundTriggerLogicalCriterion.Kind.GOES_OFF_BEFORE_TIME, 0, time));
		return  this;
	}

	public CompoundTriggerLogicalCriteria goesOffAfterTime(String trigger, Date time){
		_criteria.add(new CompoundTriggerLogicalCriterion(trigger, CompoundTriggerLogicalCriterion.Kind.GOES_OFF_AFTER_TIME, 0, time));
		return  this;
	}

	public CompoundTriggerLogicalCriteria goesOffAtTime(String trigger, Date time){
		_criteria.add(new CompoundTriggerLogicalCriterion(trigger, CompoundTriggerLogicalCriterion.Kind.GOES_OFF_AT_TIME, 0, time));
		return  this;
	}


}
