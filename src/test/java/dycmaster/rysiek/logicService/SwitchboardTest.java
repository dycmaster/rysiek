package dycmaster.rysiek.logicService;

import dycmaster.rysiek.BaseContextTestTemplate;
import dycmaster.rysiek.triggers2.InputOnlyTrigger;
import dycmaster.rysiek.triggers2.TimeAndInputTrigger;
import dycmaster.rysiek.triggers2.TimeOnlyTrigger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

import static org.junit.Assert.*;

public class SwitchboardTest extends BaseContextTestTemplate {

    @Autowired
    private Switchboard switchboard;

    @Test
    public void testInitTriggersLayout() throws Exception {

        TriggersCreator manager =new TriggersCreator();
        manager.initTriggersFromConfig(ConfigFiles.TriggerDeclarations.getPath());
        Collection<InputOnlyTrigger> inputOnlyTriggers = manager.getInputOnlyTriggers();
        Collection<TimeAndInputTrigger> timeAndInputTriggers = manager.getTimeAndInputTriggers();
        Collection<TimeOnlyTrigger> timeOnlyTriggers = manager.getTimeOnlyTriggers();

        switchboard.setTriggersAndInit(timeOnlyTriggers, inputOnlyTriggers, timeAndInputTriggers);

        switchboard.dispatchSignal("sensor2", true);
        switchboard.dispatchSignal("trigger1", true);
        switchboard.dispatchSignal("trigger2", true);

    }
}