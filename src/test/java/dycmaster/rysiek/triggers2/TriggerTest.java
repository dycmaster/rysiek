package dycmaster.rysiek.triggers2;


import dycmaster.rysiek.BaseMockitoTestTemplate;
import dycmaster.rysiek.shared.Create;
import dycmaster.rysiek.triggers2.logics.implementations.*;
import javafx.util.Pair;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

public class TriggerTest extends BaseMockitoTestTemplate {

    private static Logger log= Logger.getLogger(TriggerTest.class);

    @Test
    public void testInputOnlyTriggerWithFlipFlopLogic(){
        Map<Integer,String> declaredInputs = Create.newMap();
        InputTriggerLogic inputLogic = new FlipFlopInputLogic("flipFlop");
        InputOnlyTrigger trigger = new InputOnlyTrigger(declaredInputs, "trig1");
        trigger.setTriggerLogic(inputLogic);
        trigger.startTriggering();
        boolean initState = trigger.getOutputState();
        trigger.setInputState(1,true);
        boolean finalState = trigger.getOutputState();
        Assert.assertNotEquals("state should change due to flip-flop trigger logic", initState, finalState );
    }

    @Test
    public void testInputOnlyTriggerWithRealLogic(){
        Map<Integer, String> declaredInputs = Create.newMap();
        declaredInputs.put(1, "input1");
        declaredInputs.put(2, "input2");

        //AND
        String[] header = new String[]{"input1", "input2"};
        TruthTable truthTable = new TruthTable(header);
        truthTable.addRow(new boolean[]{true, true}, true);
        truthTable.addRow(new boolean[]{false, true}, false);
        truthTable.addRow(new boolean[]{true, false}, false);
        truthTable.addRow(new boolean[]{false, false}, false);

        InputTriggerLogic truthTableLogic = new TruthTableInputLogic("desc", truthTable);
        InputOnlyTrigger inputOnlyTrigger = new InputOnlyTrigger(declaredInputs, "input table trigger");
        inputOnlyTrigger.startTriggering();
        inputOnlyTrigger.setTriggerLogic(truthTableLogic);
        // trigger ready
        ////////////////////////////////////////
        Assert.assertFalse(inputOnlyTrigger.getOutputState());

        inputOnlyTrigger.setInputState("input1", true);
        inputOnlyTrigger.setInputState("input2", true);
        Assert.assertTrue(inputOnlyTrigger.getOutputState());

        inputOnlyTrigger.setInputState("input1", false);
        Assert.assertFalse(inputOnlyTrigger.getOutputState());

        inputOnlyTrigger.setInputState("input2", false);
        inputOnlyTrigger.setInputState("input1", true);
        Assert.assertFalse(inputOnlyTrigger.getOutputState());

        inputOnlyTrigger.setInputState("input2", false);
        inputOnlyTrigger.setInputState("input1", false);
        Assert.assertFalse(inputOnlyTrigger.getOutputState());

        inputOnlyTrigger.setInputState("input1", true);
        Assert.assertFalse(inputOnlyTrigger.getOutputState());

        log.debug("now should go true");
        inputOnlyTrigger.setInputState("input2", true);
        Assert.assertTrue(inputOnlyTrigger.getOutputState());

    }


    @Test
    public void testTimeOnlyTriggerWithLongerThanTriggerLogic() throws InterruptedException {
        TimeOnlyTrigger timeOnlyTrigger = new TimeOnlyTrigger("trig1");
        timeOnlyTrigger.startTriggering();
        TimeTriggerLogic timeLogic = new LongerThanTimeLogic("longer than test", timeOnlyTrigger, new Duration(200));
        timeOnlyTrigger.setTriggerLogic(timeLogic);

        boolean initState = timeOnlyTrigger.getOutputState();
        Thread.sleep(300);
        boolean finalState = timeOnlyTrigger.getOutputState();
        Assert.assertNotEquals("state should change ", initState, finalState );
    }


    @Test
    public void testTimeAndInputTriggerWithIsOnLongerThanLogic() throws InterruptedException {
        Map<Integer,String> declaredInputs = Create.newMap();
        declaredInputs.put(1,"input1");
        TimeAndInputTrigger timeAndInputTrigger = new TimeAndInputTrigger("trig1", declaredInputs );
        timeAndInputTrigger.startTriggering();
        TimeAndInputsTriggerLogic onLongerThanTime = new OnLongerThanTimeAndInputLogic("desc", timeAndInputTrigger,
                new Duration(100), "input1");
        timeAndInputTrigger.setTriggerLogic(onLongerThanTime);
        //now trigger is ready to be used
        //////////////////////////////////////////////

        boolean initState = timeAndInputTrigger.getOutputState();
        timeAndInputTrigger.setInputState("input1", true);
        log.debug("input set to 1. in 100 ms trigger should go true");
        Thread.sleep(200);
        boolean outputState = timeAndInputTrigger.getOutputState();
        Assert.assertFalse(initState);
        Assert.assertTrue(outputState);

        log.debug("setting input to  false. Trigger should go false immediately");
        timeAndInputTrigger.setInputState("input1", false);

        Assert.assertFalse(timeAndInputTrigger.getOutputState());
        timeAndInputTrigger.setInputState("input1", true);
        log.debug("input set to  true. Trigger should go true in 100 ms");
        Thread.sleep(50);
        Assert.assertFalse(timeAndInputTrigger.getOutputState());
        Thread.sleep(80);
        Assert.assertTrue(timeAndInputTrigger.getOutputState());

    }

    @Test
    public void testSingleShotCronLogic() throws InterruptedException {
        TimeOnlyTrigger timeOnlyTrigger = Mockito.spy(new TimeOnlyTrigger("trig1"));
        String cronString = "0/1 * * * * ?"; //every 3s
        log.debug("creating logic to run every 3s");
        TimeTriggerLogic singleShotCronLogic = new SingleShotCronTimeLogic("desc", timeOnlyTrigger, cronString );
        timeOnlyTrigger.startTriggering();
        timeOnlyTrigger.setTriggerLogic(singleShotCronLogic);
        Thread.sleep(2000);
        List<Pair<DateTime, Boolean>> triggerHistory = timeOnlyTrigger.getTriggerHistory();
        Pair<DateTime, Boolean> dateTimeBooleanPair = triggerHistory.stream().filter(a -> a.getValue().equals(true)).findFirst().get();
        Assert.assertNotNull(dateTimeBooleanPair);
        Assert.assertTrue(dateTimeBooleanPair.getValue().equals(true));
    }

}
