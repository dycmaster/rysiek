package dycmaster.rysiek.logicService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class ActionDispatcher extends AbstractActionDispatcher {
    private static Logger log = LoggerFactory.getLogger(ActionDispatcher.class);

    @Override
    public void dispatch(String triggerName, boolean state) {
        log.info(String.format("Action dispatch request received. Trigger name=%s, state=%s", triggerName, state));
        List<String> actionsToFire = findActionsToFire(triggerName, state);
        log.info("Actions to request to be fired: "+actionsToFire.toString());
        getLogicService().sendActionRequest(triggerName, state, actionsToFire);
    }
}
