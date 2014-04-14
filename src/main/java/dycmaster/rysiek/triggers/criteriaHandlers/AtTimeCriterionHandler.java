package dycmaster.rysiek.triggers.criteriaHandlers;


import dycmaster.rysiek.triggers.triggerParsers.CompoundTriggerLogicalCriterion;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

public class AtTimeCriterionHandler extends AbstractCriteriaHandler {

    protected static int atTimeTriggerCounter = 0;
    Logger log = Logger.getLogger(AtTimeCriterionHandler.class);

    protected static int getAtTimeTriggerCounter() {
        return atTimeTriggerCounter;
    }


    public AtTimeCriterionHandler(CompoundTriggerLogicalCriterion criterion) {
        super(criterion);
        atTimeTriggerCounter++;
    }

    @Override
    protected void start() {

    }

    @Override
    protected int getThisSortHandlersCounter() {
        return atTimeTriggerCounter;
    }


    @Override
    protected boolean getInitState(DateTime turnOffTime) {
        return false;
    }
}
