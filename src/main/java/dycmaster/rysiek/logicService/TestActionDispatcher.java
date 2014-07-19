package dycmaster.rysiek.logicService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestActionDispatcher implements IActionDispatcher {

    private static Logger log = LoggerFactory.getLogger(TestActionDispatcher.class);

    public void dispatch(String triggerName, boolean state) {
        log.info(String.format("action dispatch request received. Trigger name=%s, state=%s", triggerName, state));
    }
}
