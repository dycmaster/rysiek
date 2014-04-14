package dycmaster.rysiek.triggers;

import dycmaster.rysiek.shared.Create;
import dycmaster.rysiek.triggers.criteriaHandlers.AbstractCriteriaHandler;
import dycmaster.rysiek.triggers.triggerParsers.CompoundTriggerLogicalCriteria;
import dycmaster.rysiek.triggers.triggerParsers.CompoundTriggerLogicalCriterion;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;

import java.lang.reflect.Constructor;
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


	private Map<CompoundTriggerLogicalCriterion, AbstractCriteriaHandler> _criterionHandlers = new HashMap<>();


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



//	private  Collection<Timer> prepareAndApplyBeforeTimeAbsoluteTimers(
//            CompoundTriggerLogicalCriterion timeAbsoluteCriterion, Trigger trigger) {
//		Collection<Timer> result = Create.newCollection();
//
//        Date activeTo = timeAbsoluteCriterion.getTime();
//		Date currentTime = new Date();
//
//
//		//switch on every day midnight
//		result.add(switchTriggerOnEveryDayAt(00,00,1,trigger));
//
//		//switchTrigger off every day at a time given
//		result.add(switchTriggerOffEveryDayAt(activeTo, trigger));
//
//		if(beforOrEqualsHoursOnly(currentTime, activeTo)){
//            trigger.setTriggerState(true);
//		}else{
//            trigger.setTriggerState(false);
//		}
//
//		return result;
//	}
//
//
//    private static Trigger prepareOnDayTimeAbsoluteTrigger(CompoundTriggerLogicalCriterion timeAbsoluteCriterion,
//                                                           int triggerNo){
//
//
//        final Trigger absoluteTimeTrigger = new SensorTrigger();
//        absoluteTimeTrigger.setName(timeAbsoluteCriterion.getKind().name() + "_" + triggerNo);
//        absoluteTimeTrigger.start();
//
//        DateTime today = new DateTime();
//        DateTime activeDate = new DateTime(timeAbsoluteCriterion.getTime());
//
//        int result =  DateTimeComparator.getDateOnlyInstance().compare(activeDate, today);
//
//        if(result<0){ //already in the past
//            absoluteTimeTrigger.setTriggerState(false);
//
//        }else if(result == 0){ //today!
//
//            absoluteTimeTrigger.setTriggerState(true);
//            Calendar switchOffAt = GregorianCalendar.getInstance();
//            switchOffAt.setTime(new Date());
//            switchOffAt.set(Calendar.HOUR, 23);
//            switchOffAt.set(Calendar.MINUTE, 59);
//            switchOffAt.set(Calendar.SECOND, 59);
//
//            switchTriggerOffAt(switchOffAt.getTime(), absoluteTimeTrigger);
//
//        }else{ //still in the future
//
//            activeDate.hourOfDay().setCopy(0);
//            activeDate.minuteOfHour().setCopy(0);
//            activeDate.secondOfMinute().setCopy(1);
//
//            DateTime offDate = new DateTime(activeDate);
//            offDate.hourOfDay().setCopy(23);
//            offDate.minuteOfHour().setCopy(59);
//            offDate.secondOfMinute().setCopy(59);
//
//            switchTriggerOnAt(activeDate.toDate(), absoluteTimeTrigger);
//            switchTriggerOffAt(offDate.toDate(), absoluteTimeTrigger);
//        }
//        return absoluteTimeTrigger;
//    }

//    private static Trigger prepareOnDayOfMonthAbsoluteTrigger(CompoundTriggerLogicalCriterion criterion,
//                                                              int triggerNo){
//
//        DateTime dayOfMonth = new DateTime(criterion.getTime());
//        int targetDayNo = dayOfMonth.getDayOfMonth();
//        int todayDayNo = new DateTime().getDayOfMonth();
//
//        final Trigger absoluteTimeTrigger =  getNewSensorTriger(criterion.getKind().name(), triggerNo);
//
//        if(targetDayNo == todayDayNo){
//            absoluteTimeTrigger.setTriggerState(true);
//            DateTime offTime = new DateTime();
//            offTime.hourOfDay().setCopy(23);
//            offTime.minuteOfHour().setCopy(59);
//            offTime.secondOfMinute().setCopy(59);
//            switchTriggerOffAt(offTime.toDate(), absoluteTimeTrigger);
//        }else{
//            //get the closest occurrence of that day
//            LocalDate start = new LocalDate();
//            LocalDate resultCandidate =  start.withDayOfMonth(targetDayNo);
//            if(resultCandidate.isBefore(start)){
//                resultCandidate = resultCandidate.plusMonths(1);
//            }
//
//            DateTime switchOnDate = new DateTime(resultCandidate);
//            switchOnDate.hourOfDay().setCopy(0);
//            switchOnDate.minuteOfHour().setCopy(0);
//            switchOnDate.secondOfMinute().setCopy(1);
//
//            DateTime switchOffDate = new DateTime(switchOnDate);
//            switchOffDate.hourOfDay().setCopy(23);
//            switchOffDate.minuteOfHour().setCopy(59);
//            switchOffDate.secondOfMinute().setCopy(59);
//
//            switchTriggerOnAt(switchOnDate.toDate(), absoluteTimeTrigger);
//            switchTriggerOffAt(switchOffDate.toDate(), absoluteTimeTrigger);
//        }
//
//
//    }


//
//    private static Trigger prepareOnDayOfWeekAbsoluteTrigger(CompoundTriggerLogicalCriterion criterion, int triggerNo){
//
//        DateTime dayOfWeekDate = new DateTime(criterion.getTime());
//        int targetWeekday = dayOfWeekDate.getDayOfWeek();
//        int todayDay = new DateTime().getDayOfWeek();
//
//        final Trigger absoluteTimeTrigger = new SensorTrigger();
//        absoluteTimeTrigger.setName(criterion.getKind().name() + "_" + triggerNo);
//        absoluteTimeTrigger.start();
//
//        if(targetWeekday == todayDay){
//            absoluteTimeTrigger.setTriggerState(true);
//            DateTime offDate = new DateTime();
//            offDate.hourOfDay().setCopy(23);
//            offDate.minuteOfHour().setCopy(59);
//            offDate.secondOfMinute().setCopy(59);
//            switchTriggerOffAt(offDate.toDate(), absoluteTimeTrigger);
//        }else{
//            absoluteTimeTrigger.setTriggerState(false);
//            LocalDate ld = new LocalDate();
//            LocalDate targetDate = getNearestDayOfWeek(ld, targetWeekday);
//            DateTime targetOnTime = targetDate.toDateTimeAtStartOfDay();
//            DateTime targetOffTime = new DateTime(targetOnTime);
//            targetOffTime.hourOfDay().setCopy(23);
//            targetOffTime.minuteOfHour().setCopy(59);
//            targetOffTime.secondOfMinute().setCopy(59);
//
//            switchTriggerOnAt(targetOnTime.toDate(), absoluteTimeTrigger);
//            switchTriggerOffAt(targetOffTime.toDate(), absoluteTimeTrigger);
//        }
//        return absoluteTimeTrigger;
//    }
//


//	private  Trigger prepareAtTimeAbsoluteTrigger(CompoundTriggerLogicalCriterion timeAbsoluteCriterion) {
//		Date active = timeAbsoluteCriterion.getTime();
//
//		final Trigger absoluteTimeTrigger = new SensorTrigger();
//		absoluteTimeTrigger.setName(timeAbsoluteCriterion.getKind().name() + "_" + _triggers.entrySet().size());
//		absoluteTimeTrigger.start();
//
//		//switch off every day at midnight
//		switchTriggerOffEveryDayAt(23,59,59,absoluteTimeTrigger);
//
//		//switchTrigger on every day at a time given
//		switchTriggerOnEveryDayAt(active, absoluteTimeTrigger);
//
//		//switch off every day 5 sec after go off time
//		Calendar c1 = GregorianCalendar.getInstance();
//		c1.setTime(active);
//		c1.add(Calendar.SECOND, 5);
//		switchTriggerOffEveryDayAt(c1.getTime(), absoluteTimeTrigger);
//
//		absoluteTimeTrigger.setTriggerState(false);
//
//		return absoluteTimeTrigger;
//	}
//
//
//
//
//    private Trigger prepareAfterTimeAbsoluteTrigger(CompoundTriggerLogicalCriterion timeAbsoluteCriterion) {
//		Date activeFrom = timeAbsoluteCriterion.getTime();
//		Date currentTime = new Date();
//
//		final Trigger afterTimeTrigger = new SensorTrigger();
//		afterTimeTrigger.setName(timeAbsoluteCriterion.getKind().name()+"_"+_triggers.entrySet().size());
//		afterTimeTrigger.start();
//
//		//switch off every day at midnight
//		switchTriggerOffEveryDayAt(23, 59, 59, afterTimeTrigger);
//
//		//switchTrigger off every day at a time given
//		switchTriggerOnEveryDayAt(activeFrom, afterTimeTrigger);
//
//		if(beforOrEqualsHoursOnly(currentTime, activeFrom)){
//			afterTimeTrigger.setTriggerState(false);
//		}else{
//			afterTimeTrigger.setTriggerState(true);
//		}
//
//		return afterTimeTrigger;
//	}


	protected boolean isTimeAbsoluteCriterion(CompoundTriggerLogicalCriterion criterion){
		if(CompoundTriggerLogicalCriterion.TIME_ABSOLUTE_CRITERIA.contains(criterion.getKind())){
			return  true;
		}
		return false;
	}



	/**
	 * Create timers and all the mechanics to handle the criteria...
	 * @param triggerCriteria
	 */
	protected void initTrigger(CompoundTriggerLogicalCriteria triggerCriteria){


		for(CompoundTriggerLogicalCriterion criterion: triggerCriteria.getAllCriteria()){

			if(isTimeAbsoluteCriterion(criterion)){

                try{
                    Class<? extends AbstractCriteriaHandler> handlerClass = CompoundTriggerLogicalCriterion.KIND_AND_HANDLER.get(criterion.getKind());
                    Constructor<? extends AbstractCriteriaHandler> constructor =  handlerClass.getConstructor(CompoundTriggerLogicalCriterion.class);
                    AbstractCriteriaHandler criteriaHandler =  constructor.newInstance(new Object[]{criterion});
                    criteriaHandler.startHandling();
                    addTriggerToListenTo(criteriaHandler.getTriggerToObserve());
                    _criterionHandlers.put(criterion, criteriaHandler);

                }catch(Exception e){
                    logger.error(e);
                }



				if(criterion.getKind() == CompoundTriggerLogicalCriterion.Kind.AT_TIME){
				//	timeAbsoluteTrigger = prepareAtTimeAbsoluteTrigger(criterion);
				}

                if(criterion.getKind() == CompoundTriggerLogicalCriterion.Kind.ON_DAY){
                //    timeAbsoluteTrigger = prepareOnDayTimeAbsoluteTrigger(criterion, _triggers.entrySet().size());
                }

                if(criterion.getKind() == CompoundTriggerLogicalCriterion.Kind.ON_DAY_OF_WEEK ){
                 //   timeAbsoluteTrigger = prepareOnDayOfWeekAbsoluteTrigger(criterion, _triggers.entrySet().size());
                }

                if(criterion.getKind() == CompoundTriggerLogicalCriterion.Kind.ON_DAY_OF_MONTH){
                    //TODO - add handling of this criterion
                }

			}
		}

		compoundTriggerInputChanged(null, getCompoundTriggerLogicalCriteria());
	}



    //It means either:
    // 1. initTrigger just happened - in this case a changeSource parameter is null
    // 2. someObserved trigger just changed its state
	protected synchronized void compoundTriggerInputChanged(TriggerValue changeSource, CompoundTriggerLogicalCriteria triggerCriteria) {

		Collection<Boolean> parsedObservedValues = new LinkedList<>();

		//validate the criteria against the triggers
		for(CompoundTriggerLogicalCriterion criterion: triggerCriteria.getAllCriteria()){


            //Current way for time absolute triggers
            if(isTimeAbsoluteCriterion(criterion)){
                AbstractCriteriaHandler handler =  _criterionHandlers.get(criterion);
                parsedObservedValues.add(handler.getTriggerToObserve().getTriggerState());
            }

		}


		if(changeSource!=null){
			_triggerJustChangedState.put(changeSource.getSender(), false);
		}

        //Simple multiply
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
