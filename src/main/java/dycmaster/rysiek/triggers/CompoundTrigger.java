package dycmaster.rysiek.triggers;

import dycmaster.rysiek.shared.Create;
import dycmaster.rysiek.triggers.triggerParsers.CompoundTriggerLogicalCriteria;
import dycmaster.rysiek.triggers.triggerParsers.CompoundTriggerLogicalCriterion;

import java.util.*;

public class CompoundTrigger extends DefaultTrigger implements TriggerListener {


	//observed triggers
	private Map<String, Trigger> _triggers = new HashMap<>();
	//last change time of an observed trigger
	private Map<Trigger, Date> _triggerLastChangeTime = new HashMap<>();
	//current trigger state
	private Map<Trigger, Boolean> _triggerState= new HashMap<>();
	//if the change has not been handled yet
	private Map<Trigger, Boolean> _triggerJustChangedState = new HashMap<>();

	private LinkedList<Timer> _absoluteTimers = new LinkedList<>();

	private Map<CompoundTriggerLogicalCriterion, ValidationObject> _criterionValidators = new HashMap<>();


	protected void setObservedTriggerState(Trigger trigger, Boolean state){
		if(_triggers.containsKey(trigger.getName())){
			_triggerState.put(trigger, state);
		}
	}

	protected Boolean getObservedTriggerState(Trigger trigger){
		if(_triggers.containsKey(trigger.getName())){
			return  _triggerState.get(trigger);
		}
		return  null;
	}

	protected void setObservedTriggerLastChangeTime(Trigger changedTrigger){
		if(_triggers.containsKey(changedTrigger.getName())){
			_triggerLastChangeTime.put(changedTrigger, new Date());
		}
	}

	public void addTriggerToListenTo(Trigger trigger){
		trigger.subscribeToTrigger(this);
		_triggers.put(trigger.getName(), trigger);
		setObservedTriggerLastChangeTime(trigger);
		setObservedTriggerState(trigger, trigger.getTriggerState());
	}

	public void removeTriggerListenedTo(Trigger trigger){
		trigger.removeSubscriber(this);
		_triggers.remove(trigger.getName());
	}


	public CompoundTrigger(String name, CompoundTriggerLogicalCriteria logicalCriteria){
		this(name);
		setCompoundTriggerLogicalCriteria(logicalCriteria);
	}

	public CompoundTrigger(String name) {
		setName(name);
	}


	/**
	 * Handle observed trigger changes.. Some observed trigger blinked!
	 * @param triggerValue
	 */
	@Override
	public void triggerValueChangedHandler(TriggerValue triggerValue) {
		Boolean prevTriggerState = getObservedTriggerState(triggerValue.getSender());
		Boolean currTriggerState = triggerValue.getTriggerStatus();

		if(Boolean.compare(prevTriggerState, currTriggerState)!=0){
			setObservedTriggerState(triggerValue.getSender(), triggerValue.getTriggerStatus());
			setObservedTriggerLastChangeTime(triggerValue.getSender());
			_triggerJustChangedState.put(triggerValue.getSender(), true);
		}

		compoundTriggerInputChanged(triggerValue, getCompoundTriggerLogicalCriteria());
	}



	protected long getSameTimeToday(Date date){

		Calendar dateCal = GregorianCalendar.getInstance();
		dateCal.setTime(date);
		int hour = dateCal.get(Calendar.HOUR_OF_DAY);
		int min = dateCal.get(Calendar.MINUTE);
		int sec = dateCal.get(Calendar.SECOND);

		return  getTodayTime(hour, min, sec);
	}


	protected long getTodayTime(int hourOfDay, int min, int sec){
		Calendar nowCal =  GregorianCalendar.getInstance();
		nowCal.setTime(new Date());
		nowCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
		nowCal.set(Calendar.MINUTE, min);
		nowCal.set(Calendar.SECOND, sec);

		return  nowCal.getTime().getTime();
	}


	protected void switchTriggerOnOffEveryDayAt(int hourOfDay, int min, int sec, final Trigger trigger, final boolean targetState){

		Date switchOnFirstRun = new Date(getTodayTime(hourOfDay, min, sec));
		Date now = new Date();

		if(now.after(switchOnFirstRun)){
			switchOnFirstRun.setTime(switchOnFirstRun.getTime()+(24*60*60*1000));
		}

		long period = 24*60*60*1000;

		Timer switchOnTimer = new Timer();
		switchOnTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				trigger.setTriggerState(targetState);
			}
		}, switchOnFirstRun, period);
	}



	protected void switchTriggerOnEveryDayAt(int hourOfDay, int min, int sec, final Trigger trigger){
		switchTriggerOnOffEveryDayAt(hourOfDay, min, sec,trigger,true);
	}

	protected void switchTriggerOffEveryDayAt(int hourOfDay, int min, int sec, final Trigger trigger){
		switchTriggerOnOffEveryDayAt(hourOfDay, min, sec,trigger,false);
	}

	protected void switchTriggerOffEveryDayAt(Date date,  final Trigger trigger){

		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);

		switchTriggerOnOffEveryDayAt(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
				cal.get(Calendar.SECOND), trigger, false);
	}

	protected void switchTriggerOnEveryDayAt(Date date,  final Trigger trigger){

		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);

		switchTriggerOnOffEveryDayAt(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
				cal.get(Calendar.SECOND), trigger, true);
	}


	/*
	Tells if d1 is before d2 (or the same)
	 */
	protected boolean beforOrEqualsHoursOnly(Date d1, Date d2){
		return  ! afterHoursOnly(d1, d2);
	}

	/*
	Tells if d1 is after d2 or not
	 */
	protected boolean afterHoursOnly(Date d1, Date d2){
		Calendar c1 = GregorianCalendar.getInstance();
		Calendar c2 = GregorianCalendar.getInstance();

		c1.setTime(d1);
		c2.setTime(d2);

		if( c1.get(Calendar.HOUR_OF_DAY) > c2.get(Calendar.HOUR_OF_DAY)){
			return  true;
		}else if( c1.get(Calendar.HOUR_OF_DAY) < c2.get(Calendar.HOUR_OF_DAY)){
			return  false;
		}else {
			if( c1.get(Calendar.MINUTE) > c2.get(Calendar.MINUTE)){
				return  true;
			}else if( c1.get(Calendar.MINUTE) < c2.get(Calendar.MINUTE)){
				return  false;
			}else{
				if( c1.get(Calendar.SECOND) > c2.get(Calendar.SECOND)){
					return  true;
				}else if( c1.get(Calendar.SECOND) < c2.get(Calendar.SECOND)){
					return  false;
				}else{
					return  false;
				}
			}
		}
	}

	private Trigger prepareBeforeTimeAbsoluteTrigger(CompoundTriggerLogicalCriterion timeAbsoluteCriterion) {
		Date activeTo = timeAbsoluteCriterion.getTime();
		Date currentTime = new Date();

		final Trigger beforeTimeTrigger = new SensorTrigger();
		beforeTimeTrigger.setName(timeAbsoluteCriterion.getKind().name()+"_"+_triggers.entrySet().size());
		beforeTimeTrigger.start();

		//switch on every day at midnight
		switchTriggerOnEveryDayAt(23,59,59,beforeTimeTrigger);

		//switchTrigger off every day at a time given
		switchTriggerOffEveryDayAt(activeTo, beforeTimeTrigger);

		if(beforOrEqualsHoursOnly(currentTime, activeTo)){
			beforeTimeTrigger.setTriggerState(true);
		}else{
			beforeTimeTrigger.setTriggerState(false);
		}

		return beforeTimeTrigger;
	}


	private Trigger prepareAtTimeAbsoluteTrigger(CompoundTriggerLogicalCriterion timeAbsoluteCriterion) {
		Date active = timeAbsoluteCriterion.getTime();
		Date currentTime = new Date();

		final Trigger absoluteTimeTrigger = new SensorTrigger();
		absoluteTimeTrigger.setName(timeAbsoluteCriterion.getKind().name() + "_" + _triggers.entrySet().size());
		absoluteTimeTrigger.start();

		//switch off every day at midnight
		switchTriggerOffEveryDayAt(23,59,59,absoluteTimeTrigger);

		//switchTrigger on every day at a time given
		switchTriggerOnEveryDayAt(active, absoluteTimeTrigger);

		//switch off every day 5 sec after go off time
		Calendar c1 = GregorianCalendar.getInstance();
		c1.setTime(active);
		c1.add(Calendar.SECOND, 5);
		switchTriggerOffEveryDayAt(c1.getTime(), absoluteTimeTrigger);

		absoluteTimeTrigger.setTriggerState(false);

		return absoluteTimeTrigger;
	}

	private Trigger prepareAfterTimeAbsoluteTrigger(CompoundTriggerLogicalCriterion timeAbsoluteCriterion) {
		Date activeFrom = timeAbsoluteCriterion.getTime();
		Date currentTime = new Date();

		final Trigger afterTimeTrigger = new SensorTrigger();
		afterTimeTrigger.setName(timeAbsoluteCriterion.getKind().name()+"_"+_triggers.entrySet().size());
		afterTimeTrigger.start();

		//switch off every day at midnight
		switchTriggerOffEveryDayAt(23, 59, 59, afterTimeTrigger);

		//switchTrigger off every day at a time given
		switchTriggerOnEveryDayAt(activeFrom, afterTimeTrigger);

		if(beforOrEqualsHoursOnly(currentTime, activeFrom)){
			afterTimeTrigger.setTriggerState(false);
		}else{
			afterTimeTrigger.setTriggerState(true);
		}

		return afterTimeTrigger;
	}


	protected boolean isTimeAbsoluteCriterion(CompoundTriggerLogicalCriterion criterion){
		if(CompoundTriggerLogicalCriterion.TIME_ABSOLUTE_CRITERIA.contains(criterion.getKind())){
			return  true;
		}
		return false;
	}

	protected Collection<CompoundTriggerLogicalCriterion> getTimeAbsoluteCriteria(CompoundTriggerLogicalCriteria triggerCriteria){
		Collection<CompoundTriggerLogicalCriterion> result = Create.newCollection();
		for(CompoundTriggerLogicalCriterion criterion: triggerCriteria.getAllCriteria()){
			if(CompoundTriggerLogicalCriterion.TIME_ABSOLUTE_CRITERIA.contains(criterion.getKind())){
				result.add(criterion);
			}
		}
		return result;
	}

	protected static  boolean containsSomeTimeAbsoluteCriteria(CompoundTriggerLogicalCriteria triggerCriteria){
		for(CompoundTriggerLogicalCriterion criterion: triggerCriteria.getAllCriteria()){
			if(CompoundTriggerLogicalCriterion.TIME_ABSOLUTE_CRITERIA.contains(criterion.getKind())){
				return  true;
			}
		}
		return  false;
	}


	/**
	 * Create timers and all the mechanics to handle the criteria...
	 * @param triggerCriteria
	 */
	protected void initTrigger(CompoundTriggerLogicalCriteria triggerCriteria){


		for(CompoundTriggerLogicalCriterion criterion: triggerCriteria.getAllCriteria()){

			if(isTimeAbsoluteCriterion(criterion)){

				Trigger timeAbsoluteTrigger = null;

				if(criterion.getKind() == CompoundTriggerLogicalCriterion.Kind.BEFORE_TIME){
					timeAbsoluteTrigger = prepareBeforeTimeAbsoluteTrigger(criterion);
				}

				if(criterion.getKind() == CompoundTriggerLogicalCriterion.Kind.AFTER_TIME){
					timeAbsoluteTrigger = prepareAfterTimeAbsoluteTrigger(criterion);
				}

				if(criterion.getKind() == CompoundTriggerLogicalCriterion.Kind.AT_TIME){
					timeAbsoluteTrigger = prepareAtTimeAbsoluteTrigger(criterion);
				}

				addTriggerToListenTo(timeAbsoluteTrigger);
				ValidationObject validationObject = new ValidationObject(criterion);
				validationObject.getTriggers().add(timeAbsoluteTrigger);
				_criterionValidators.put(criterion, validationObject);
			}


		}


		compoundTriggerInputChanged(null, getCompoundTriggerLogicalCriteria());
	}



	protected synchronized void compoundTriggerInputChanged(TriggerValue changeSource, CompoundTriggerLogicalCriteria triggerCriteria) {

		Collection<Boolean> parsedObservedValues = new LinkedList<>();

		//validate the criteria against the triggers
		for(CompoundTriggerLogicalCriterion criterion: triggerCriteria.getAllCriteria()){
			ValidationObject vo = _criterionValidators.get(criterion);

			if(vo.getCriterionToValidate().getKind().equals(CompoundTriggerLogicalCriterion.Kind.BEFORE_TIME)
					|| vo.getCriterionToValidate().getKind().equals(CompoundTriggerLogicalCriterion.Kind.AFTER_TIME)
					|| vo.getCriterionToValidate().getKind().equals(CompoundTriggerLogicalCriterion.Kind.AT_TIME)){
				parsedObservedValues.add(vo.getTriggers().iterator().next().getTriggerState());
			}

		}





		//TODO

		if(changeSource!=null){
			_triggerJustChangedState.put(changeSource.getSender(), false);
		}

		Boolean compTriggerValue = true;
		for(Boolean val:   parsedObservedValues){
			if(val.equals(false)){
				compTriggerValue = false;
				break;
			}
		}
		setTriggerState(compTriggerValue);
	}


	/**
	 * We want parameters from all the observed triggers!
	 * @param triggerValue
	 * @return
	 */
	protected Map<String,Boolean> getTriggerInput(TriggerValue triggerValue) {
		Map<String,Boolean> params = new HashMap<>();
		for(Map.Entry<String, Trigger> triggerEntry: _triggers.entrySet()){
			params.put(triggerEntry.getKey(), triggerEntry.getValue().getTriggerState());
		}
		return  params;
	}


	CompoundTriggerLogicalCriteria _compoundTriggerLogicalCriteria;

	public void setCompoundTriggerLogicalCriteria(CompoundTriggerLogicalCriteria logicalCriteria){
		_compoundTriggerLogicalCriteria = logicalCriteria;
		initTrigger(logicalCriteria);
	}

	public CompoundTriggerLogicalCriteria getCompoundTriggerLogicalCriteria(){

		return  _compoundTriggerLogicalCriteria;
	}




	// Not used here...
	@Override
	protected Map<String, String> getTriggerInput() {
		return null;
	}

}
