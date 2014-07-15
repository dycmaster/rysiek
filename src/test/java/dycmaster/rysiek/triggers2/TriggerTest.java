package dycmaster.rysiek.triggers2;


import dycmaster.rysiek.BaseMockitoTestTemplate;
import dycmaster.rysiek.shared.Create;
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
    public void testInputOnlyTrigger_FlipFlopLogic(){
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
    public void testInputOnlyTrigger_FlipFlopLogic_builder(){
        InputOnlyTrigger trigger = new InputOnlyTrigger.Builder(Create.newMap(), "trig1")
                .withLogicType(TriggerLogics.FlipFlopInputLogic.getLogicStringName())
                .withLogicDescription("desc")
                .build();

        boolean initState = trigger.getOutputState();
        trigger.setInputState(1,true);
        boolean finalState = trigger.getOutputState();
        Assert.assertNotEquals("state should change due to flip-flop trigger logic", initState, finalState );
    }

    @Test
    public void testInputOnlyTrigger_TruthTableLogic(){
        Map<Integer, String> declaredInputs = Create.newMap();
        declaredInputs.put(1, "input1");
        declaredInputs.put(2, "input2");

        //AND
        String[] header = new String[]{"input1", "input2"};
        TruthTable truthTable = new TruthTable(header);
        truthTable.addRow(new Boolean[]{true, true}, true);
        truthTable.addRow(new Boolean[]{false, true}, false);
        truthTable.addRow(new Boolean[]{true, false}, false);
        truthTable.addRow(new Boolean[]{false, false}, false);

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
    public void testInputOnlyTrigger_TruthTableLogic_builder(){

        Map<Integer, String> declaredInputs = Create.newMap();
        declaredInputs.put(1, "input1");
        declaredInputs.put(2, "input2");

        //AND
        String[] header = new String[]{"input1", "input2"};
        TruthTable truthTable = new TruthTable(header);
        truthTable.addRow(new Boolean[]{true, true}, true);
        truthTable.addRow(new Boolean[]{false, true}, false);
        truthTable.addRow(new Boolean[]{true, false}, false);
        truthTable.addRow(new Boolean[]{false, false}, false);

        InputOnlyTrigger inputOnlyTrigger = new InputOnlyTrigger.Builder(declaredInputs, "name")
                .withLogicType(TriggerLogics.TruthTableInputLogic.getLogicStringName())
                .withLogicDescription("desc")
                .withTruthTable(truthTable).build();


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
    public void test_TimeOnlyTrigger_LongerThanTriggerLogic() throws InterruptedException {
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
    public void test_TimeOnlyTrigger_LongerThanTriggerLogic_builder() throws InterruptedException {
        TimeOnlyTrigger timeOnlyTrigger = new TimeOnlyTrigger.Builder("trig1")
                .withLogicType(TriggerLogics.LongerThanTimeLogic.getLogicStringName())
                .withLogicDescription("desc")
                .withLogicDuration(200)
                .build();

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
    public void testTimeAndInputTriggerWithIsOnLongerThanLogic_builder() throws InterruptedException {
        Map<Integer,String> declaredInputs = Create.newMap();
        declaredInputs.put(1,"input1");
        TimeAndInputTrigger timeAndInputTrigger = new TimeAndInputTrigger.Builder("name", declaredInputs)
                .withLogicType(TriggerLogics.OnLongerThanTime.getLogicStringName())
                .withInputTrackedByLogic("input1")
                .withLogicDelayWhenTrackingInput(100)
                .withLogicDescription("desc")
                .build();

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
    public void test_SingleShotCronLogic() throws InterruptedException {
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

    @Test
    public void test_SingleShotCronLogic_builder() throws InterruptedException {
        String cronString = "0/1 * * * * ?"; //every 3s
        TimeOnlyTrigger timeOnlyTrigger = new TimeOnlyTrigger.Builder("name")
                .withLogicType(TriggerLogics.SingleShotCronTimeLogic.getLogicStringName())
                .withLogicDescription("desc")
                .withLogicCronString(cronString)
                .build();

        Thread.sleep(2000);
        List<Pair<DateTime, Boolean>> triggerHistory = timeOnlyTrigger.getTriggerHistory();
        Pair<DateTime, Boolean> dateTimeBooleanPair = triggerHistory.stream().filter(a -> a.getValue().equals(true)).findFirst().get();
        Assert.assertNotNull(dateTimeBooleanPair);
        Assert.assertTrue(dateTimeBooleanPair.getValue().equals(true));
    }

}
