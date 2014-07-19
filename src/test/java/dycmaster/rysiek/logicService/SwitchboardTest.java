package dycmaster.rysiek.logicService;

import dycmaster.rysiek.BaseContextTestTemplate;
import dycmaster.rysiek.triggers2.InputOnlyTrigger;
import dycmaster.rysiek.triggers2.TimeAndInputTrigger;
import dycmaster.rysiek.triggers2.TimeOnlyTrigger;
import javafx.util.Pair;
import org.javatuples.Triplet;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.*;

public class SwitchboardTest extends BaseContextTestTemplate {

    @Test
    public void testInitTriggersLayout() throws Exception {

        TriggersCreator manager =new TriggersCreator();
        manager.initTriggersFromConfig(ConfigFiles.TriggerDeclarations.getUrl());
        Collection<InputOnlyTrigger> inputOnlyTriggers = manager.getInputOnlyTriggers();
        Collection<TimeAndInputTrigger> timeAndInputTriggers = manager.getTimeAndInputTriggers();
        Collection<TimeOnlyTrigger> timeOnlyTriggers = manager.getTimeOnlyTriggers();


        Switchboard switchboard = new Switchboard();
        IActionDispatcher actionDispatcher = new TestActionDispatcher();
        switchboard.setActionDispatcher(actionDispatcher);
        switchboard.setInputOnlyTriggers(inputOnlyTriggers);
        switchboard.setTimeOnlyTriggers(timeOnlyTriggers);
        switchboard.setTimeAndInputTriggers(timeAndInputTriggers);
        switchboard.init();
        switchboard.dispatchSignal("sensor2", true);
        switchboard.dispatchSignal("trigger1", true);
        switchboard.dispatchSignal("trigger2", true);

        Thread.sleep(1000);

        assertNotNull(true);
    }
}