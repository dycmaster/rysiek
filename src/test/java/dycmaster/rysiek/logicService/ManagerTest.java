package dycmaster.rysiek.logicService;

import dycmaster.rysiek.BaseContextTestTemplate;
import dycmaster.rysiek.triggers2.InputOnlyTrigger;
import dycmaster.rysiek.triggers2.TimeAndInputTrigger;
import dycmaster.rysiek.triggers2.TimeOnlyTrigger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Collection;

import static org.junit.Assert.*;

public class ManagerTest extends BaseContextTestTemplate {

    @Autowired
    private TriggersCreator manager;

    @Test
    public void testInitTriggersFromConfig() throws IOException {
        manager.initTriggersFromConfig(ConfigFiles.TriggerDeclarations.getPath());
        Collection<InputOnlyTrigger> inputOnlyTriggers = manager.getInputOnlyTriggers();
        Collection<TimeAndInputTrigger> timeAndInputTriggers = manager.getTimeAndInputTriggers();
        Collection<TimeOnlyTrigger> timeOnlyTriggers = manager.getTimeOnlyTriggers();

        assertEquals(1, timeAndInputTriggers.size());
        assertEquals(2, timeOnlyTriggers.size());
        assertEquals(2, inputOnlyTriggers.size());

    }

}