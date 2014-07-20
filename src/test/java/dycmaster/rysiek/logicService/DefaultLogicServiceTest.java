package dycmaster.rysiek.logicService;

import dycmaster.rysiek.BaseContextTestTemplate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class DefaultLogicServiceTest extends BaseContextTestTemplate {

    @Autowired
    private ILogicService logicService;

    @Autowired
    private  TriggersCreator triggersCreator;

    @Test
    public void testInit() throws Exception {
        logicService.getSwitchboard().setTriggersAndInit(triggersCreator.getTimeOnlyTriggers(),
                triggersCreator.getInputOnlyTriggers(), triggersCreator.getTimeAndInputTriggers());


        Thread.sleep(300000);

    }
}